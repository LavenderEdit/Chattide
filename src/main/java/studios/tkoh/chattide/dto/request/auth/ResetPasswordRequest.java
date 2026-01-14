package studios.tkoh.chattide.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Studios TKOH!
 */
public record ResetPasswordRequest(
        @NotBlank(message = "El token/OTP es obligatorio")
        String token,
        @NotBlank(message = "La nueva contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String newPassword,
        @NotBlank(message = "La confirmación de contraseña es obligatoria")
        String confirmPassword) {

}
