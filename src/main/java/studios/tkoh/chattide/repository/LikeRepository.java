package studios.tkoh.chattide.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studios.tkoh.chattide.model.Like;

/**
 *
 * @author Studios TKOH!
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    // Para saber si "Yo" ya le di like a este post (pintar el corazón de rojo)
    boolean existsByUsuarioIdAndPublicacionId(Long usuarioId, Long publicacionId);

    // Para quitar el like (Dislike)
    Optional<Like> findByUsuarioIdAndPublicacionId(Long usuarioId, Long publicacionId);

    // Contar likes totales de un post
    long countByPublicacionId(Long publicacionId);
}
