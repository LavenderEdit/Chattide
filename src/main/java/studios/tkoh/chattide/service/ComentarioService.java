package studios.tkoh.chattide.service;

import java.util.List;
import studios.tkoh.chattide.dto.request.ComentarioRequest;
import studios.tkoh.chattide.dto.response.ComentarioResponse;

/**
 *
 * @author Studios TKOH!
 */
public interface ComentarioService {

    ComentarioResponse agregarComentario(ComentarioRequest request);

    void eliminarComentario(Long comentarioId, Long usuarioSolicitanteId);

    List<ComentarioResponse> obtenerComentariosDePost(Long publicacionId);
}
