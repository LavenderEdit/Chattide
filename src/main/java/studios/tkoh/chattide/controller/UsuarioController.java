package studios.tkoh.chattide.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import studios.tkoh.chattide.dto.request.BusquedaRequest;
import studios.tkoh.chattide.dto.request.UsuarioUpdateRequest;
import studios.tkoh.chattide.dto.response.UsuarioResponse;
import studios.tkoh.chattide.service.UsuarioService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<UsuarioResponse> obtenerPerfil(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(usuarioService.obtenerPerfil(usuarioId));
    }

    @PutMapping("/{usuarioId}")
    @PreAuthorize("@accessControlService.isSameUser(#usuarioId)")
    public ResponseEntity<UsuarioResponse> actualizarPerfil(
            @PathVariable Long usuarioId,
            @RequestBody UsuarioUpdateRequest request) {
        return ResponseEntity.ok(usuarioService.actualizarPerfil(usuarioId, request));
    }

    @PutMapping(value = "/{usuarioId}/avatar", consumes = "multipart/form-data")
    @PreAuthorize("@accessControlService.isSameUser(#usuarioId)")
    public ResponseEntity<UsuarioResponse> actualizarAvatar(
            @PathVariable Long usuarioId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(usuarioService.actualizarAvatar(usuarioId, file));
    }

    @PostMapping("/buscar")
    public ResponseEntity<Page<UsuarioResponse>> buscarUsuarios(
            @RequestBody BusquedaRequest request,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(usuarioService.buscarUsuarios(request, pageable));
    }
}
