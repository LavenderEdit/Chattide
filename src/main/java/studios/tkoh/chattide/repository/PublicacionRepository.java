package studios.tkoh.chattide.repository;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studios.tkoh.chattide.model.Publicacion;

/**
 *
 * @author Studios TKOH!
 */
@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    Page<Publicacion> findByGrupoIdOrderByFechaCreacionDesc(Long grupoId, Pageable pageable);

    Page<Publicacion> findByUsuarioIdAndGrupoIsNullOrderByFechaCreacionDesc(Long usuarioId, Pageable pageable);

    // Feed Global: Ver posts de todos los grupos a los que pertenezco (Requiere Query compleja, lo dejare para fase avanzada)
}
