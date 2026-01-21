package studios.tkoh.chattide.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.chattide.dto.request.LikeRequest;
import studios.tkoh.chattide.exception.ResourceNotFoundException;
import studios.tkoh.chattide.model.*;
import studios.tkoh.chattide.repository.*;
import studios.tkoh.chattide.service.LikeService;
import java.util.Optional;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final LikeComentarioRepository likeComentarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PublicacionRepository publicacionRepository;
    private final ComentarioRepository comentarioRepository;

    @Override
    @Transactional
    public void toggleLike(LikeRequest request, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        if ("POST".equalsIgnoreCase(request.entityType())) {
            gestionarLikePublicacion(request.entityId(), usuario);
        } else if ("COMMENT".equalsIgnoreCase(request.entityType())) {
            gestionarLikeComentario(request.entityId(), usuario);
        }
    }

    private void gestionarLikePublicacion(Long publicacionId, Usuario usuario) {
        Optional<Like> existingLike = likeRepository.findByUsuarioIdAndPublicacionId(usuario.getId(), publicacionId);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            Publicacion publicacion = publicacionRepository.findById(publicacionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Publicación", "id", publicacionId));

            Like like = Like.builder().usuario(usuario).publicacion(publicacion).build();
            likeRepository.save(like);
        }
    }

    private void gestionarLikeComentario(Long comentarioId, Usuario usuario) {
        Optional<LikeComentario> existingLike = likeComentarioRepository.findByUsuarioIdAndComentarioId(usuario.getId(), comentarioId);

        if (existingLike.isPresent()) {
            likeComentarioRepository.delete(existingLike.get());
        } else {
            Comentario comentario = comentarioRepository.findById(comentarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Comentario", "id", comentarioId));

            LikeComentario like = LikeComentario.builder().usuario(usuario).comentario(comentario).build();
            likeComentarioRepository.save(like);
        }
    }
}
