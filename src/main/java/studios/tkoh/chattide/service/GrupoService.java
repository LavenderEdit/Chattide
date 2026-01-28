package studios.tkoh.chattide.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import studios.tkoh.chattide.dto.request.BusquedaRequest;
import studios.tkoh.chattide.dto.request.GrupoRequest;
import studios.tkoh.chattide.dto.request.MemberActionRequest;
import studios.tkoh.chattide.dto.response.GrupoResponse;

/**
 *
 * @author Studios TKOH!
 */
public interface GrupoService {

    GrupoResponse crearGrupo(GrupoRequest request);

    GrupoResponse obtenerGrupo(Long grupoId);

    GrupoResponse obtenerDetalleGrupo(Long grupoId);

    GrupoResponse editarGrupo(Long grupoId, GrupoRequest request);

    void eliminarGrupo(Long grupoId);

    void unirseGrupo(Long grupoId, Long usuarioId);

    void salirGrupo(Long grupoId, Long usuarioId);

    // Gestión avanzada
    void gestionarMiembro(MemberActionRequest request, Long usuarioSolicitanteId); // Expulsar, Promover

    void transferirGrupo(MemberActionRequest request, Long usuarioSolicitanteId);

    // Método auxiliar para el controlador
    void eliminarMiembro(Long grupoId, Long usuarioId);

    Page<GrupoResponse> buscarGrupos(BusquedaRequest request, Pageable pageable);

    List<GrupoResponse> misGrupos(Long usuarioId);
}
