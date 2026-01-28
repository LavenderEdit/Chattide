package studios.tkoh.chattide.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studios.tkoh.chattide.dto.request.LikeRequest;
import studios.tkoh.chattide.security.AccessControlService;
import studios.tkoh.chattide.service.LikeService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final AccessControlService accessControlService;

    @PostMapping
    public ResponseEntity<Void> toggleLike(@Valid @RequestBody LikeRequest request) {
        // Obtenemos el ID del usuario directamente del token de seguridad
        Long usuarioId = accessControlService.getCurrentUserId();

        likeService.toggleLike(request.entityId(), request.entityType(), usuarioId);

        return ResponseEntity.ok().build();
    }
}
