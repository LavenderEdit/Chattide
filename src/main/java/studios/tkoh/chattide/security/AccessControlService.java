package studios.tkoh.chattide.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.chattide.model.Comentario;
import studios.tkoh.chattide.model.Publicacion;
import studios.tkoh.chattide.model.enums.AppRole;
import studios.tkoh.chattide.model.enums.GroupRole;
import studios.tkoh.chattide.repository.ComentarioRepository;
import studios.tkoh.chattide.repository.PublicacionRepository;
import studios.tkoh.chattide.repository.UsuarioGrupoRepository;
import studios.tkoh.chattide.repository.UsuarioRepository;

import java.util.Optional;

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
    private final ComentarioRepository comentarioRepository;
    private final JwtService jwtService;
    private final HttpServletRequest request;

    // --- User Checks ---
    // Checks if the authenticated user is the same as the requested userId.
    public boolean isSameUser(Long targetUserId) {
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(targetUserId);
    }

    // Checks if the current user is a global ADMIN.
    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(AppRole.ROLE_ADMIN.name()));
    }

    // --- Post Checks ---
    // Permission to delete a post: 1. Global Admin 2. Post Owner 3. Group
    // Admin/Owner (if post belongs to a group)
    @Transactional(readOnly = true)
    public boolean canDeletePost(Long postId) {
        if (isAdmin()) {
            return true;
        }

        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return false;
        }

        Optional<Publicacion> postOpt = publicacionRepository.findById(postId);
        if (postOpt.isEmpty()) {
            return false;
        }
        Publicacion post = postOpt.get();

        // 1. Owner
        if (post.getUsuario().getId().equals(currentUserId)) {
            return true;
        }

        // 2. Group Admin check
        if (post.getGrupo() != null) {
            return isGroupAdminOrOwner(currentUserId, post.getGrupo().getId());
        }

        return false;
    }

    public boolean isPostOwner(Long postId) {
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && publicacionRepository.findById(postId)
                .map(post -> post.getUsuario().getId().equals(currentUserId))
                .orElse(false);
    }

    // --- Group Checks ---
    // Check if user is OWNER of the group.
    public boolean isGroupOwner(Long grupoId) {
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && usuarioGrupoRepository.findByUsuarioIdAndGrupoId(currentUserId, grupoId)
                .map(ug -> GroupRole.OWNER.name().equals(ug.getRol()))
                .orElse(false);
    }

    // Check if user is ADMIN or OWNER of the group.
    public boolean isGroupAdmin(Long grupoId) {
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && isGroupAdminOrOwner(currentUserId, grupoId);
    }

    // --- Comment Checks ---
    public boolean canDeleteComment(Long commentId) {
        if (isAdmin()) {
            return true;
        }

        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return false;
        }

        Optional<Comentario> commentOpt = comentarioRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            return false;
        }
        Comentario comment = commentOpt.get();

        // 1. Comment Owner
        if (comment.getUsuario().getId().equals(currentUserId)) {
            return true;
        }

        // 2. Post Owner (The person who owns the post can delete comments on it)
        if (comment.getPublicacion().getUsuario().getId().equals(currentUserId)) {
            return true;
        }

        // 3. Group Admin (If post is in a group)
        if (comment.getPublicacion().getGrupo() != null) {
            return isGroupAdminOrOwner(currentUserId, comment.getPublicacion().getGrupo().getId());
        }

        return false;
    }

    // --- Helpers ---
    private boolean isGroupAdminOrOwner(Long userId, Long grupoId) {
        return usuarioGrupoRepository.findByUsuarioIdAndGrupoId(userId, grupoId)
                .map(ug -> GroupRole.ADMIN.name().equals(ug.getRol()) || GroupRole.OWNER.name().equals(ug.getRol()))
                .orElse(false);
    }

    public Long getCurrentUserId() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                // Try extracting ID from claims first (if you added it to JWT generation)
                // If not, fallback to email lookup
                String email = jwtService.extractUsername(token);
                return usuarioRepository.findByCorreo(email)
                        .map(u -> u.getId())
                        .orElse(null);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
