package studios.tkoh.chattide.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.chattide.dto.request.PublicacionRequest;
import studios.tkoh.chattide.dto.response.PublicacionResponse;
import studios.tkoh.chattide.exception.ChattideException;
import studios.tkoh.chattide.exception.ResourceNotFoundException;
import studios.tkoh.chattide.mapper.PublicacionMapper;
import studios.tkoh.chattide.model.Publicacion;
import studios.tkoh.chattide.repository.PublicacionRepository;
import studios.tkoh.chattide.service.PublicacionService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class PublicacionServiceImpl implements PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final PublicacionMapper publicacionMapper;

    @Override
    @Transactional
    public PublicacionResponse crearPublicacion(PublicacionRequest request) {
        // Aquí se valida si el usuario es miembro del grupo antes de publicar
        Publicacion publicacion = publicacionMapper.toEntity(request);
        return publicacionMapper.toResponse(publicacionRepository.save(publicacion));
    }

    @Override
    @Transactional
    public PublicacionResponse editarPublicacion(Long publicacionId, PublicacionRequest request) {
        Publicacion publicacion = getOrThrow(publicacionId);

        if (!publicacion.getUsuario().getId().equals(request.usuarioId())) {
            throw new ChattideException("No tienes permiso para editar esta publicación");
        }

        publicacionMapper.updateEntityFromRequest(request, publicacion);
        return publicacionMapper.toResponse(publicacionRepository.save(publicacion));
    }

    @Override
    @Transactional
    public void eliminarPublicacion(Long publicacionId, Long usuarioSolicitanteId) {
        Publicacion publicacion = getOrThrow(publicacionId);

        // Lógica: Puede borrar el dueño O un admin del grupo
        boolean esDueno = publicacion.getUsuario().getId().equals(usuarioSolicitanteId);
        // boolean esAdminGrupo = ... (Lógica para verificar si usuarioSolicitante es admin del grupo)

        if (!esDueno) {
            throw new ChattideException("No tienes permiso para eliminar esta publicación");
        }

        publicacionRepository.delete(publicacion);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublicacionResponse> obtenerFeedGrupo(Long grupoId, Pageable pageable) {
        return publicacionRepository.findByGrupoIdOrderByFechaCreacionDesc(grupoId, pageable)
                .map(publicacionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublicacionResponse> obtenerMuroUsuario(Long usuarioId, Pageable pageable) {
        return publicacionRepository.findByUsuarioIdAndGrupoIsNullOrderByFechaCreacionDesc(usuarioId, pageable)
                .map(publicacionMapper::toResponse);
    }

    private Publicacion getOrThrow(Long id) {
        return publicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación", "id", id));
    }
}
