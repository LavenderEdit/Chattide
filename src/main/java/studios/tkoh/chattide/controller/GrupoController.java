package studios.tkoh.chattide.controller;

import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.security.access.prepost.PreAuthorize;
import studios.tkoh.chattide.security.AccessControlService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/grupos")
@RequiredArgsConstructor
public class GrupoController {

    private final GrupoService grupoService;
    private final AccessControlService accessControlService;

    @PostMapping
    public ResponseEntity<GrupoResponse> crearGrupo(@RequestBody GrupoRequest request) {
        if (!accessControlService.isSameUser(request.creadorId())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(grupoService.crearGrupo(request));
    }

    @GetMapping("/{grupoId}")
    public ResponseEntity<GrupoResponse> obtenerGrupo(@PathVariable Long grupoId) {
        return ResponseEntity.ok(grupoService.obtenerGrupo(grupoId));
    }

    @PutMapping("/{grupoId}")
    @PreAuthorize("@accessControlService.isGroupOwner(#grupoId)")
    public ResponseEntity<GrupoResponse> editarGrupo(
            @PathVariable Long grupoId,
            @RequestBody GrupoRequest request) {
        return ResponseEntity.ok(grupoService.editarGrupo(grupoId, request));
    }

    @DeleteMapping("/{grupoId}")
    @PreAuthorize("@accessControlService.isGroupOwner(#grupoId) or hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarGrupo(@PathVariable Long grupoId) {
        grupoService.eliminarGrupo(grupoId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{grupoId}/unirse")
    public ResponseEntity<Void> unirseGrupo(
            @PathVariable Long grupoId,
            @RequestBody MemberActionRequest request) {
        if (!accessControlService.isSameUser(request.targetUsuarioId())) {
            return ResponseEntity.status(403).build();
        }
        grupoService.unirseGrupo(grupoId, request.targetUsuarioId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{grupoId}/salir")
    public ResponseEntity<Void> salirGrupo(
            @PathVariable Long grupoId,
            @RequestBody MemberActionRequest request) {
        if (!accessControlService.isSameUser(request.targetUsuarioId())) {
            return ResponseEntity.status(403).build();
        }
        grupoService.salirGrupo(grupoId, request.targetUsuarioId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{grupoId}/miembros")
    @PreAuthorize("@accessControlService.isGroupAdmin(#grupoId)")
    public ResponseEntity<Void> eliminarMiembro(
            @PathVariable Long grupoId,
            @RequestBody MemberActionRequest request) {
        grupoService.eliminarMiembro(grupoId, request.targetUsuarioId()); // targetUsuarioId viene en el body
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transferir")
    @PreAuthorize("@accessControlService.isGroupOwner(#request.grupoId())")
    public ResponseEntity<Void> transferirGrupo(@RequestBody MemberActionRequest request) {
        // El usuarioId en el request es el target (nuevo dueño), el solicitante es el actual (del token)
        grupoService.transferirGrupo(request, accessControlService.getCurrentUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mis-grupos")
    public ResponseEntity<List<GrupoResponse>> misGrupos() {
        Long currentUserId = accessControlService.getCurrentUserId();
        return ResponseEntity.ok(grupoService.misGrupos(currentUserId));
    }

    @PostMapping("/buscar")
    public ResponseEntity<Page<GrupoResponse>> buscarGrupos(
            @RequestBody BusquedaRequest request,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(grupoService.buscarGrupos(request, pageable));
    }
}
