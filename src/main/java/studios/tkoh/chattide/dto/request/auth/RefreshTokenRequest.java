package studios.tkoh.chattide.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author Studios TKOH!
 */
public record RefreshTokenRequest(
        @NotBlank(message = "El Refresh Token es obligatorio")
        String refreshToken) {

}
