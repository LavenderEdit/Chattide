package studios.tkoh.chattide.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import studios.tkoh.chattide.dto.request.BusquedaRequest;
import studios.tkoh.chattide.dto.request.UsuarioUpdateRequest;
import studios.tkoh.chattide.dto.response.UsuarioResponse;

/**
 *
 * @author Studios TKOH!
 */
public interface UsuarioService {

    UsuarioResponse obtenerPerfil(Long usuarioId);

    UsuarioResponse actualizarPerfil(Long usuarioId, UsuarioUpdateRequest request);

    Page<UsuarioResponse> buscarUsuarios(BusquedaRequest request, Pageable pageable);
}
