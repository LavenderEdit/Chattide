package studios.tkoh.chattide.repository;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studios.tkoh.chattide.model.Usuario;

/**
 *
 * @author Studios TKOH!
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    @Query("SELECT u FROM Usuario u WHERE lower(u.nombre) LIKE lower(concat('%', :query, '%')) OR lower(u.apellido) LIKE lower(concat('%', :query, '%'))")
    Page<Usuario> buscarUsuarios(@Param("query") String query, Pageable pageable);
}
