package studios.tkoh.chattide.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studios.tkoh.chattide.dto.request.ComentarioRequest;
import studios.tkoh.chattide.dto.response.ComentarioResponse;
import studios.tkoh.chattide.service.ComentarioService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import studios.tkoh.chattide.security.AccessControlService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;
    private final AccessControlService accessControlService;

    @PostMapping
    public ResponseEntity<ComentarioResponse> agregarComentario(@RequestBody ComentarioRequest request) {
        if (!accessControlService.isSameUser(request.usuarioId())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(comentarioService.agregarComentario(request));
    }

    @DeleteMapping("/{comentarioId}")
    @PreAuthorize("@accessControlService.canDeleteComment(#comentarioId)")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long comentarioId) {
        comentarioService.eliminarComentario(comentarioId, accessControlService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/publicacion/{publicacionId}")
    public ResponseEntity<List<ComentarioResponse>> obtenerComentarios(@PathVariable Long publicacionId) {
        return ResponseEntity.ok(comentarioService.obtenerComentariosDePost(publicacionId));
    }
}
