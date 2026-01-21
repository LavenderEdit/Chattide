package studios.tkoh.chattide.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import studios.tkoh.chattide.model.RefreshToken;
import studios.tkoh.chattide.model.Usuario;

/**
 *
 * @author Studios TKOH!
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    // Borrar tokens de un usuario (Logout total)
    @Modifying
    void deleteByUsuario(Usuario usuario);
}
