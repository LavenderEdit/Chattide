package studios.tkoh.chattide.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studios.tkoh.chattide.model.PasswordResetToken;

/**
 *
 * @author Studios TKOH!
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    // Útil para limpiezas de mantenimiento - mantenerlo por ahora atte. Joan
    void deleteByUsuarioId(Long usuarioId);
}
