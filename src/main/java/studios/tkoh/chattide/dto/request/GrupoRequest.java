package studios.tkoh.chattide.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Studios TKOH!
 */
public record GrupoRequest(
        @NotBlank(message = "El nombre del grupo es obligatorio")
        String nombre,
        String descripcion,
        @NotNull(message = "El ID del creador es obligatorio")
        Long creadorId) {

}
