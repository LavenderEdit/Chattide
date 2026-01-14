package studios.tkoh.chattide.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author Studios TKOH!
 */
public record LoginRequest(
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Formato de correo inválido")
        String correo,
        @NotBlank(message = "La contraseña es obligatoria")
        String password) {

}
