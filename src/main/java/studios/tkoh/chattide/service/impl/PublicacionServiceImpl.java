package studios.tkoh.chattide.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import studios.tkoh.chattide.dto.request.PublicacionRequest;
import studios.tkoh.chattide.dto.response.PublicacionResponse;
import studios.tkoh.chattide.exception.ChattideException;
import studios.tkoh.chattide.exception.ResourceNotFoundException;
import studios.tkoh.chattide.mapper.PublicacionMapper;
import studios.tkoh.chattide.model.Publicacion;
import studios.tkoh.chattide.model.Usuario;
import studios.tkoh.chattide.repository.PublicacionRepository;
import studios.tkoh.chattide.repository.UsuarioRepository;
import studios.tkoh.chattide.service.GoogleDriveService;
import studios.tkoh.chattide.service.PublicacionService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class PublicacionServiceImpl implements PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final PublicacionMapper publicacionMapper;
    private final GoogleDriveService googleDriveService;

    @Override
    @Transactional
    public PublicacionResponse crearPublicacion(PublicacionRequest request, MultipartFile image) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.usuarioId()));

        Publicacion publicacion = publicacionMapper.toEntity(request);
        publicacion.setUsuario(usuario);

        // Upload image if present
        if (image != null && !image.isEmpty()) {
            String imageUrl = googleDriveService.uploadPostImage(usuario, image);
            publicacion.setImagenUrl(imageUrl);
        }

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

        boolean esDueno = publicacion.getUsuario().getId().equals(usuarioSolicitanteId);
        if (!esDueno) {
            throw new ChattideException("No tienes permiso para eliminar esta publicación");
        }

        // Delete image from Drive if it exists
        if (publicacion.getImagenUrl() != null && publicacion.getImagenUrl().contains("drive.google.com")) {
            googleDriveService.deleteFile(publicacion.getImagenUrl());
        }

        publicacionRepository.delete(publicacion);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublicacionResponse> obtenerFeedGrupo(Long grupoId, Pageable pageable) {
        return publicacionRepository.findByGrupoIdOrderByFechaRegistroDesc(grupoId, pageable)
                .map(publicacionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublicacionResponse> obtenerMuroUsuario(Long usuarioId, Pageable pageable) {
        return publicacionRepository.findByUsuarioIdAndGrupoIsNullOrderByFechaRegistroDesc(usuarioId, pageable)
                .map(publicacionMapper::toResponse);
    }

    private Publicacion getOrThrow(Long id) {
        return publicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación", "id", id));
    }
}
