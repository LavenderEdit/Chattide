package studios.tkoh.chattide.dto.response;

import java.time.LocalDateTime;

/**
 *
 * @author Studios TKOH!
 */
public record ComentarioResponse(
        Long id,
        String contenido,
        LocalDateTime fechaComentario,
        UsuarioResponse autor) {

}
