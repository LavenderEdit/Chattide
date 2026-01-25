package studios.tkoh.chattide.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studios.tkoh.chattide.dto.request.BusquedaRequest;
import studios.tkoh.chattide.dto.request.GrupoRequest;
import studios.tkoh.chattide.dto.request.MemberActionRequest;
import studios.tkoh.chattide.dto.response.GrupoResponse;
import studios.tkoh.chattide.service.GrupoService;
import java.util.List;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/grupos")
@RequiredArgsConstructor
public class GrupoController {

    private final GrupoService grupoService;

    @PostMapping
    public ResponseEntity<GrupoResponse> crearGrupo(@Valid @RequestBody GrupoRequest request) {
        return ResponseEntity.ok(grupoService.crearGrupo(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrupoResponse> obtenerGrupo(@PathVariable Long id) {
        return ResponseEntity.ok(grupoService.obtenerDetalleGrupo(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrupoResponse> editarGrupo(@PathVariable Long id, @Valid @RequestBody GrupoRequest request) {
        return ResponseEntity.ok(grupoService.editarGrupo(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGrupo(@PathVariable Long id, @RequestParam Long usuarioSolicitanteId) {
        grupoService.eliminarGrupo(id, usuarioSolicitanteId);
        return ResponseEntity.noContent().build();
    }

    // --- Gestión de Miembros ---
    @PostMapping("/{id}/unirse")
    public ResponseEntity<String> unirseGrupo(@PathVariable Long id, @RequestParam Long usuarioId) {
        grupoService.unirseGrupo(id, usuarioId);
        return ResponseEntity.ok("Te has unido al grupo exitosamente");
    }

    @PostMapping("/{id}/salir")
    public ResponseEntity<String> salirGrupo(@PathVariable Long id, @RequestParam Long usuarioId) {
        grupoService.salirGrupo(id, usuarioId);
        return ResponseEntity.ok("Has salido del grupo");
    }

    @PostMapping("/transferir")
    public ResponseEntity<String> transferirGrupo(@Valid @RequestBody MemberActionRequest request, @RequestParam Long solicitanteId) {
        grupoService.transferirGrupo(request, solicitanteId);
        return ResponseEntity.ok("Propiedad del grupo transferida");
    }

    // --- Búsquedas ---
    @GetMapping("/buscar")
    public ResponseEntity<Page<GrupoResponse>> buscarGrupos(
            @RequestParam String query,
            @PageableDefault(size = 10) Pageable pageable) {
        BusquedaRequest request = new BusquedaRequest(query, pageable.getPageNumber(), pageable.getPageSize(), null, null);
        return ResponseEntity.ok(grupoService.buscarGrupos(request, pageable));
    }

    @GetMapping("/mis-grupos")
    public ResponseEntity<List<GrupoResponse>> misGrupos(@RequestParam Long usuarioId) {
        return ResponseEntity.ok(grupoService.misGrupos(usuarioId));
    }
}
