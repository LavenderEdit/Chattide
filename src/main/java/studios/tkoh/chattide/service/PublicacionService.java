package studios.tkoh.chattide.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import studios.tkoh.chattide.dto.request.PublicacionRequest;
import studios.tkoh.chattide.dto.response.PublicacionResponse;

/**
 *
 * @author Studios TKOH!
 */
public interface PublicacionService {

    PublicacionResponse crearPublicacion(PublicacionRequest request, MultipartFile image);

    PublicacionResponse editarPublicacion(Long publicacionId, PublicacionRequest request);

    void eliminarPublicacion(Long publicacionId, Long usuarioSolicitanteId);

    Page<PublicacionResponse> obtenerFeedGrupo(Long grupoId, Pageable pageable);

    Page<PublicacionResponse> obtenerMuroUsuario(Long usuarioId, Pageable pageable);
}
