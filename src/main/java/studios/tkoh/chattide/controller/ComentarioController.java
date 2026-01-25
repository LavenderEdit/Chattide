package studios.tkoh.chattide.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studios.tkoh.chattide.dto.request.ComentarioRequest;
import studios.tkoh.chattide.dto.response.ComentarioResponse;
import studios.tkoh.chattide.service.ComentarioService;
import java.util.List;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    @PostMapping
    public ResponseEntity<ComentarioResponse> agregarComentario(@Valid @RequestBody ComentarioRequest request) {
        return ResponseEntity.ok(comentarioService.agregarComentario(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long id, @RequestParam Long usuarioSolicitanteId) {
        comentarioService.eliminarComentario(id, usuarioSolicitanteId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{publicacionId}")
    public ResponseEntity<List<ComentarioResponse>> obtenerComentarios(@PathVariable Long publicacionId) {
        return ResponseEntity.ok(comentarioService.obtenerComentariosDePost(publicacionId));
    }
}
