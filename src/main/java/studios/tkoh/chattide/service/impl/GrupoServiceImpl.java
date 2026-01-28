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
import studios.tkoh.chattide.model.enums.GroupRole;
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
        if (grupoRepository.existsByNombre(request.nombre())) {
            throw new ChattideException("Ya existe un grupo con ese nombre");
        }

        Usuario creador = usuarioRepository.findById(request.creadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.creadorId()));

        Grupo grupo = grupoMapper.toEntity(request);
        grupo.setCreador(creador);

        Grupo guardado = grupoRepository.save(grupo);

        // Añadir al creador como OWNER
        UsuarioGrupo membresia = UsuarioGrupo.builder()
                .usuario(creador)
                .grupo(guardado)
                .rol(GroupRole.OWNER.name())
                .build();

        usuarioGrupoRepository.save(membresia);

        return grupoMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoResponse obtenerGrupo(Long grupoId) {
        return grupoMapper.toResponse(getGrupoOrThrow(grupoId));
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoResponse obtenerDetalleGrupo(Long grupoId) {
        return obtenerGrupo(grupoId);
    }

    @Override
    @Transactional
    public GrupoResponse editarGrupo(Long grupoId, GrupoRequest request) {
        Grupo grupo = getGrupoOrThrow(grupoId);

        // Actualizamos campos básicos
        grupo.setNombre(request.nombre());
        grupo.setDescripcion(request.descripcion());

        return grupoMapper.toResponse(grupoRepository.save(grupo));
    }

    @Override
    @Transactional
    public void eliminarGrupo(Long grupoId) {
        Grupo grupo = getGrupoOrThrow(grupoId);
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
                .rol(GroupRole.MEMBER.name())
                .build();

        usuarioGrupoRepository.save(membresia);
    }

    @Override
    @Transactional
    public void salirGrupo(Long grupoId, Long usuarioId) {
        UsuarioGrupo membresia = usuarioGrupoRepository.findByUsuarioIdAndGrupoId(usuarioId, grupoId)
                .orElseThrow(() -> new ChattideException("No eres miembro de este grupo"));

        if (GroupRole.OWNER.name().equals(membresia.getRol())) {
            throw new ChattideException("El creador no puede salir del grupo sin transferirlo o eliminarlo");
        }

        usuarioGrupoRepository.delete(membresia);
    }

    @Override
    @Transactional
    public void gestionarMiembro(MemberActionRequest request, Long usuarioSolicitanteId) {
        // Implementación básica: eliminar miembro (expulsar)
        eliminarMiembro(request.grupoId(), request.targetUsuarioId());
    }

    @Override
    @Transactional
    public void eliminarMiembro(Long grupoId, Long usuarioId) {
        UsuarioGrupo membresia = usuarioGrupoRepository.findByUsuarioIdAndGrupoId(usuarioId, grupoId)
                .orElseThrow(() -> new ResourceNotFoundException("Miembro", "usuarioId", usuarioId));

        if (GroupRole.OWNER.name().equals(membresia.getRol())) {
            throw new ChattideException("No se puede expulsar al creador del grupo");
        }

        usuarioGrupoRepository.delete(membresia);
    }

    @Override
    @Transactional
    public void transferirGrupo(MemberActionRequest request, Long usuarioSolicitanteId) {
        Grupo grupo = getGrupoOrThrow(request.grupoId());

        // Verificación extra de seguridad
        if (!grupo.getCreador().getId().equals(usuarioSolicitanteId)) {
            throw new ChattideException("Solo el creador puede transferir el grupo");
        }

        Usuario nuevoDueno = usuarioRepository.findById(request.targetUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.targetUsuarioId()));

        // Buscar membresía del nuevo dueño
        UsuarioGrupo membresiaNuevo = usuarioGrupoRepository.findByUsuarioIdAndGrupoId(nuevoDueno.getId(), grupo.getId())
                .orElseThrow(() -> new ChattideException("El nuevo dueño debe ser miembro del grupo"));

        // Buscar membresía del dueño actual
        UsuarioGrupo membresiaActual = usuarioGrupoRepository.findByUsuarioIdAndGrupoId(usuarioSolicitanteId, grupo.getId())
                .orElseThrow(() -> new ChattideException("Error interno: Dueño no es miembro"));

        // Intercambio de roles
        membresiaNuevo.setRol(GroupRole.OWNER.name());
        membresiaActual.setRol(GroupRole.ADMIN.name()); // El antiguo dueño pasa a ser admin

        usuarioGrupoRepository.save(membresiaNuevo);
        usuarioGrupoRepository.save(membresiaActual);

        // Actualizar referencia en entidad Grupo
        grupo.setCreador(nuevoDueno);
        grupoRepository.save(grupo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GrupoResponse> buscarGrupos(BusquedaRequest request, Pageable pageable) {
        // Restaurado para usar el método que pediste
        return grupoRepository.findByNombreContainingIgnoreCase(request.query(), pageable)
                .map(grupoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponse> misGrupos(Long usuarioId) {
        List<UsuarioGrupo> membresias = usuarioGrupoRepository.findByUsuarioId(usuarioId);
        return grupoMapper.toResponseList(membresias.stream().map(UsuarioGrupo::getGrupo).toList());
    }

    private Grupo getGrupoOrThrow(Long id) {
        return grupoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", id));
    }
}
