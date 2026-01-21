package studios.tkoh.chattide.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.chattide.dto.request.BusquedaRequest;
import studios.tkoh.chattide.dto.request.GrupoRequest;
import studios.tkoh.chattide.dto.request.MemberActionRequest;
import studios.tkoh.chattide.dto.response.GrupoResponse;
import studios.tkoh.chattide.exception.ChattideException;
import studios.tkoh.chattide.exception.ResourceNotFoundException;
import studios.tkoh.chattide.mapper.GrupoMapper;
import studios.tkoh.chattide.model.Grupo;
import studios.tkoh.chattide.model.Usuario;
import studios.tkoh.chattide.model.UsuarioGrupo;
import studios.tkoh.chattide.repository.GrupoRepository;
import studios.tkoh.chattide.repository.UsuarioGrupoRepository;
import studios.tkoh.chattide.repository.UsuarioRepository;
import studios.tkoh.chattide.service.GrupoService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class GrupoServiceImpl implements GrupoService {

    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioGrupoRepository usuarioGrupoRepository;
    private final GrupoMapper grupoMapper;

    @Override
    @Transactional
    public GrupoResponse crearGrupo(GrupoRequest request) {
        Usuario creador = usuarioRepository.findById(request.creadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.creadorId()));

        Grupo grupo = grupoMapper.toEntity(request);
        grupo.setCreador(creador);

        Grupo grupoGuardado = grupoRepository.save(grupo);

        // Añadir al creador como ADMIN automáticamente
        UsuarioGrupo membresia = UsuarioGrupo.builder()
                .usuario(creador)
                .grupo(grupoGuardado)
                .rol("ADMIN") // Role constante
                .build();
        usuarioGrupoRepository.save(membresia);

        return grupoMapper.toResponse(grupoGuardado);
    }

    @Override
    @Transactional
    public GrupoResponse editarGrupo(Long grupoId, GrupoRequest request) {
        Grupo grupo = getGrupoOrThrow(grupoId);
        validarPermisoAdmin(grupo, request.creadorId()); // Usamos creadorId como el ID de quien hace la request

        grupoMapper.updateEntityFromRequest(request, grupo);
        return grupoMapper.toResponse(grupoRepository.save(grupo));
    }

    @Override
    @Transactional
    public void eliminarGrupo(Long grupoId, Long usuarioSolicitanteId) {
        Grupo grupo = getGrupoOrThrow(grupoId);

        // Solo el creador original puede eliminar el grupo
        if (!grupo.getCreador().getId().equals(usuarioSolicitanteId)) {
            throw new ChattideException("Solo el creador puede eliminar el grupo");
        }

        grupoRepository.delete(grupo);
    }

    @Override
    @Transactional
    public void unirseGrupo(Long grupoId, Long usuarioId) {
        if (usuarioGrupoRepository.existsByUsuarioIdAndGrupoId(usuarioId, grupoId)) {
            throw new ChattideException("Ya eres miembro de este grupo");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));
        Grupo grupo = getGrupoOrThrow(grupoId);

        UsuarioGrupo membresia = UsuarioGrupo.builder()
                .usuario(usuario)
                .grupo(grupo)
                .rol("MIEMBRO")
                .build();

        usuarioGrupoRepository.save(membresia);
    }

    @Override
    @Transactional
    public void salirGrupo(Long grupoId, Long usuarioId) {
        UsuarioGrupo membresia = usuarioGrupoRepository.findByUsuarioIdAndGrupoId(usuarioId, grupoId)
                .orElseThrow(() -> new ChattideException("No eres miembro de este grupo"));

        // Lógica adicional: Si el creador sale, ¿qué pasa? Por ahora simple:
        if ("ADMIN".equals(membresia.getRol()) && membresia.getGrupo().getCreador().getId().equals(usuarioId)) {
            throw new ChattideException("El creador no puede salir sin transferir el grupo antes");
        }

        usuarioGrupoRepository.delete(membresia);
    }

    @Override
    @Transactional
    public void gestionarMiembro(MemberActionRequest request, Long usuarioSolicitanteId) {
        // Implementación futura: Verificar si solicitante es ADMIN y cambiar rol o eliminar miembro
    }

    @Override
    @Transactional
    public void transferirGrupo(MemberActionRequest request, Long usuarioSolicitanteId) {
        Grupo grupo = getGrupoOrThrow(request.grupoId());

        if (!grupo.getCreador().getId().equals(usuarioSolicitanteId)) {
            throw new ChattideException("Solo el creador puede transferir el grupo");
        }

        Usuario nuevoDueno = usuarioRepository.findById(request.targetUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.targetUsuarioId()));

        // Asegurar que el nuevo dueño es miembro y hacerlo admin
        UsuarioGrupo membresiaNuevo = usuarioGrupoRepository.findByUsuarioIdAndGrupoId(nuevoDueno.getId(), grupo.getId())
                .orElseThrow(() -> new ChattideException("El nuevo dueño debe ser miembro del grupo"));

        membresiaNuevo.setRol("ADMIN");
        usuarioGrupoRepository.save(membresiaNuevo);

        // Cambiar dueño
        grupo.setCreador(nuevoDueno);
        grupoRepository.save(grupo);
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoResponse obtenerDetalleGrupo(Long grupoId) {
        return grupoMapper.toResponse(getGrupoOrThrow(grupoId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GrupoResponse> buscarGrupos(BusquedaRequest request, Pageable pageable) {
        return grupoRepository.findByNombreContainingIgnoreCase(request.query(), pageable)
                .map(grupoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponse> misGrupos(Long usuarioId) {
        List<UsuarioGrupo> membresias = usuarioGrupoRepository.findByUsuarioId(usuarioId);
        return grupoMapper.toResponseList(membresias.stream().map(UsuarioGrupo::getGrupo).toList());
    }

    // Helpers
    private Grupo getGrupoOrThrow(Long id) {
        return grupoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", id));
    }

    private void validarPermisoAdmin(Grupo grupo, Long usuarioId) {
        UsuarioGrupo ug = usuarioGrupoRepository.findByUsuarioIdAndGrupoId(usuarioId, grupo.getId())
                .orElseThrow(() -> new ChattideException("No eres miembro del grupo"));
        if (!"ADMIN".equals(ug.getRol())) {
            throw new ChattideException("No tienes permisos de administrador");
        }
    }
}
