package studios.tkoh.chattide.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studios.tkoh.chattide.model.LikeComentario;

/**
 *
 * @author Studios TKOH!
 */
@Repository
public interface LikeComentarioRepository extends JpaRepository<LikeComentario, Long> {

    boolean existsByUsuarioIdAndComentarioId(Long usuarioId, Long comentarioId);

    Optional<LikeComentario> findByUsuarioIdAndComentarioId(Long usuarioId, Long comentarioId);

    long countByComentarioId(Long comentarioId);
}
