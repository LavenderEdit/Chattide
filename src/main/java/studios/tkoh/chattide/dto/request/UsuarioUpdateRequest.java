package studios.tkoh.chattide.dto.request;

import jakarta.validation.constraints.Size;

/**
 *
 * @author Studios TKOH!
 */
public record UsuarioUpdateRequest(
        @Size(min = 2, max = 100)
        String nombre,
        @Size(min = 2, max = 100)
        String apellido,
        String fotoPerfil,
        String estado) {

}
