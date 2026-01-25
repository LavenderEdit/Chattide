package studios.tkoh.chattide.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPerfil(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPerfil(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarPerfil(
            @PathVariable Long id,
            @RequestBody UsuarioUpdateRequest request) {
        // TODO: Validar que el ID del token coincida con el ID de la URL (Seguridad)
        return ResponseEntity.ok(usuarioService.actualizarPerfil(id, request));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<UsuarioResponse>> buscarUsuarios(
            @RequestParam String query,
            @PageableDefault(size = 10) Pageable pageable) {
        // Adaptamos los params de URL al DTO de búsqueda
        BusquedaRequest request = new BusquedaRequest(query, pageable.getPageNumber(), pageable.getPageSize(), null, null);
        return ResponseEntity.ok(usuarioService.buscarUsuarios(request, pageable));
    }
}
