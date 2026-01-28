package studios.tkoh.chattide.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import studios.tkoh.chattide.repository.PublicacionRepository;
import studios.tkoh.chattide.repository.UsuarioGrupoRepository;
import studios.tkoh.chattide.repository.UsuarioRepository;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class AccessControlService {

    private final PublicacionRepository publicacionRepository;
    private final UsuarioGrupoRepository usuarioGrupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final HttpServletRequest request;

    public boolean isPostOwner(Long postId) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return publicacionRepository.findById(postId)
                .map(post -> post.getUsuario().getCorreo().equals(currentUserEmail))
                .orElse(false);
    }

    public boolean isGroupAdmin(Long grupoId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return false;
        }

        return usuarioGrupoRepository.findByUsuarioIdAndGrupoId(userId, grupoId)
                .map(ug -> "ADMIN".equals(ug.getRol()) || "OWNER".equals(ug.getRol()))
                .orElse(false);
    }

    /**
     * Extracts the User ID from the JWT token in the current request. Falls
     * back to database lookup via email if the ID claim is missing.
     */
    private Long getCurrentUserId() {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Integer jwtUserId = jwtService.extractClaim(token, claims -> claims.get("userId", Integer.class));
                if (jwtUserId != null) {
                    return jwtUserId.longValue();
                }
            } catch (Exception e) {
                // Token might be valid but missing the claim or parsing failed
            }
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email != null) {
            return usuarioRepository.findByCorreo(email)
                    .map(user -> user.getId())
                    .orElse(null);
        }

        return null;
    }
}
