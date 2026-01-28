package studios.tkoh.chattide.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.chattide.exception.ResourceNotFoundException;
import studios.tkoh.chattide.model.*;
import studios.tkoh.chattide.repository.*;
import studios.tkoh.chattide.service.LikeService;
import java.util.Optional;
import studios.tkoh.chattide.exception.ChattideException;
import studios.tkoh.chattide.model.enums.EntityType;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final LikeComentarioRepository likeComentarioRepository;
    private final PublicacionRepository publicacionRepository;
    private final ComentarioRepository comentarioRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void toggleLike(Long entityId, EntityType entityType, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        switch (entityType) {
            case PUBLICACION ->
                togglePublicacionLike(entityId, usuario);
            case COMENTARIO ->
                toggleComentarioLike(entityId, usuario);
            default ->
                throw new ChattideException("Tipo de entidad no soportado para Likes");
        }
    }

    private void togglePublicacionLike(Long publicacionId, Usuario usuario) {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", publicacionId));

        Optional<Like> existingLike = likeRepository.findByUsuarioIdAndPublicacionId(usuario.getId(), publicacionId);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            Like like = Like.builder()
                    .usuario(usuario)
                    .publicacion(publicacion)
                    .build();
            likeRepository.save(like);
        }
    }

    private void toggleComentarioLike(Long comentarioId, Usuario usuario) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario", "id", comentarioId));

        Optional<LikeComentario> existingLike = likeComentarioRepository.findByUsuarioIdAndComentarioId(usuario.getId(), comentarioId);

        if (existingLike.isPresent()) {
            likeComentarioRepository.delete(existingLike.get());
        } else {
            LikeComentario like = LikeComentario.builder()
                    .usuario(usuario)
                    .comentario(comentario)
                    .build();
            likeComentarioRepository.save(like);
        }
    }
}
