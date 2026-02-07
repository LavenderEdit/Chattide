package studios.tkoh.chattide.service.impl;

import java.util.ArrayList;
import java.util.List;
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
import studios.tkoh.chattide.model.enums.GroupRole;
import studios.tkoh.chattide.repository.PublicacionRepository;
import studios.tkoh.chattide.repository.UsuarioGrupoRepository;
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
    private final UsuarioGrupoRepository usuarioGrupoRepository;
    private final PublicacionMapper publicacionMapper;
    private final GoogleDriveService googleDriveService;

    @Override
    @Transactional
    public PublicacionResponse crearPublicacion(PublicacionRequest request, List<MultipartFile> images) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.usuarioId()));

        Publicacion publicacion = publicacionMapper.toEntity(request);
        publicacion.setUsuario(usuario);

        // Lógica de Multi-Upload
        List<String> uploadedUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile img : images) {
                if (!img.isEmpty()) {
                    String url = googleDriveService.uploadPostImage(usuario, img);
                    uploadedUrls.add(url);
                }
            }
        }
        publicacion.setImagenesUrls(uploadedUrls);

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
        publicacion.setEsEditado(true);
        return publicacionMapper.toResponse(publicacionRepository.save(publicacion));
    }

    @Override
    @Transactional
    public void eliminarPublicacion(Long publicacionId, Long usuarioSolicitanteId) {
        Publicacion publicacion = getOrThrow(publicacionId);

        boolean esDueno = publicacion.getUsuario().getId().equals(usuarioSolicitanteId);
        boolean esAdminGrupo = false;

        if (publicacion.getGrupo() != null) {
            esAdminGrupo = usuarioGrupoRepository.findByUsuarioIdAndGrupoId(usuarioSolicitanteId, publicacion.getGrupo().getId())
                    .map(ug -> GroupRole.ADMIN.name().equals(ug.getRol()) || GroupRole.OWNER.name().equals(ug.getRol()))
                    .orElse(false);
        }

        if (!esDueno && !esAdminGrupo) {
            throw new ChattideException("No tienes permiso para eliminar esta publicación");
        }

        if (publicacion.getImagenesUrls() != null) {
            for (String url : publicacion.getImagenesUrls()) {
                googleDriveService.deleteFile(url);
            }
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
