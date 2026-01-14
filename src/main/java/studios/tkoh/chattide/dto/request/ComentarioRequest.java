package studios.tkoh.chattide.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Studios TKOH!
 */
public record ComentarioRequest(
        @NotBlank(message = "El comentario no puede estar vacío")
        String contenido,
        @NotNull(message = "El usuario es obligatorio")
        Long usuarioId,
        @NotNull(message = "La publicación es obligatoria")
        Long publicacionId) {

}
