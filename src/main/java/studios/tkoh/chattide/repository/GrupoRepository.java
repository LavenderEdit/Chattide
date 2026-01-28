package studios.tkoh.chattide.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    boolean existsByNombre(String nombre);

    // Consulta opcional: Busca por nombre O descripción
    @Query("SELECT g FROM Grupo g WHERE LOWER(g.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(g.descripcion) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Grupo> busquedaAvanzada(@Param("query") String query, Pageable pageable);
}
