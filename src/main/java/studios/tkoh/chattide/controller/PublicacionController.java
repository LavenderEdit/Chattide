package studios.tkoh.chattide.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studios.tkoh.chattide.dto.request.PublicacionRequest;
import studios.tkoh.chattide.dto.response.PublicacionResponse;
import studios.tkoh.chattide.service.PublicacionService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/publicaciones")
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionService publicacionService;

    @PostMapping
    public ResponseEntity<PublicacionResponse> crearPublicacion(@Valid @RequestBody PublicacionRequest request) {
        return ResponseEntity.ok(publicacionService.crearPublicacion(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublicacionResponse> editarPublicacion(
            @PathVariable Long id,
            @Valid @RequestBody PublicacionRequest request) {
        return ResponseEntity.ok(publicacionService.editarPublicacion(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPublicacion(@PathVariable Long id, @RequestParam Long usuarioSolicitanteId) {
        publicacionService.eliminarPublicacion(id, usuarioSolicitanteId);
        return ResponseEntity.noContent().build();
    }

    // Feeds
    @GetMapping("/feed/grupo/{grupoId}")
    public ResponseEntity<Page<PublicacionResponse>> obtenerFeedGrupo(
            @PathVariable Long grupoId,
            @PageableDefault(size = 20, sort = "fechaCreacion", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(publicacionService.obtenerFeedGrupo(grupoId, pageable));
    }

    @GetMapping("/feed/usuario/{usuarioId}")
    public ResponseEntity<Page<PublicacionResponse>> obtenerMuroUsuario(
            @PathVariable Long usuarioId,
            @PageableDefault(size = 20, sort = "fechaCreacion", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(publicacionService.obtenerMuroUsuario(usuarioId, pageable));
    }
}
