package studios.tkoh.chattide.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studios.tkoh.chattide.dto.request.LikeRequest;
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

    @PostMapping
    public ResponseEntity<String> toggleLike(@Valid @RequestBody LikeRequest request, @RequestParam Long usuarioId) {
        likeService.toggleLike(request, usuarioId);
        return ResponseEntity.ok("Like actualizado");
    }
}
