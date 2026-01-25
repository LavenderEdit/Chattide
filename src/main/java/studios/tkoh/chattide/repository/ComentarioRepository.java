package studios.tkoh.chattide.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studios.tkoh.chattide.model.Comentario;

/**
 *
 * @author Studios TKOH!
 */
@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    // Cargar comentarios de un post
    List<Comentario> findByPublicacionIdOrderByFechaRegistroAsc(Long publicacionId);

    // Contar comentarios (para mostrar "5 comentarios" en la tarjeta del post)
    long countByPublicacionId(Long publicacionId);
}
