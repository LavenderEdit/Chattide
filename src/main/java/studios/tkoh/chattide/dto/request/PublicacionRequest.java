package studios.tkoh.chattide.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Studios TKOH!
 */
public record PublicacionRequest(
        String titulo,
        @NotBlank(message = "El contenido no puede estar vacío")
        String contenido,
        @NotNull(message = "El usuario es obligatorio")
        Long usuarioId,
        Long grupoId // Futuro: List<String> tags, PrivacyLevel privacy
        ) {

}
