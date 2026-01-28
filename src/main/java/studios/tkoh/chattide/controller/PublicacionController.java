package studios.tkoh.chattide.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import studios.tkoh.chattide.dto.request.PublicacionRequest;
import studios.tkoh.chattide.dto.response.PublicacionResponse;
import studios.tkoh.chattide.security.AccessControlService;
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
    private final AccessControlService accessControlService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PublicacionResponse> crearPublicacion(
            @RequestPart("data") PublicacionRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        // Additional check: Ensure the user creating the post is the one logged in
        if (!accessControlService.isSameUser(request.usuarioId())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(publicacionService.crearPublicacion(request, file));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@accessControlService.isPostOwner(#id)")
    public ResponseEntity<PublicacionResponse> editarPublicacion(
            @PathVariable Long id,
            @RequestBody PublicacionRequest request) {
        return ResponseEntity.ok(publicacionService.editarPublicacion(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@accessControlService.canDeletePost(#id)")
    public ResponseEntity<Void> eliminarPublicacion(@PathVariable Long id) {
        // We pass the current user ID to the service for additional logic if needed, 
        // but the security check is already handled by PreAuthorize
        publicacionService.eliminarPublicacion(id, accessControlService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/grupo/{grupoId}")
    public ResponseEntity<Page<PublicacionResponse>> obtenerFeedGrupo(
            @PathVariable Long grupoId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(publicacionService.obtenerFeedGrupo(grupoId, pageable));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<PublicacionResponse>> obtenerMuroUsuario(
            @PathVariable Long usuarioId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(publicacionService.obtenerMuroUsuario(usuarioId, pageable));
    }
}
