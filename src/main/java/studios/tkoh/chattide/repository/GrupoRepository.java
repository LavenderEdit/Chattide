package studios.tkoh.chattide.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studios.tkoh.chattide.model.Grupo;

/**
 *
 * @author Studios TKOH!
 */
@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    Page<Grupo> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    List<Grupo> findByCreadorId(Long creadorId);
}
