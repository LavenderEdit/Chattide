package studios.tkoh.chattide.dto.response.auth;

import studios.tkoh.chattide.dto.response.UsuarioResponse;

/**
 *
 * @author Studios TKOH!
 */
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn,
        UsuarioResponse usuario) {

}
