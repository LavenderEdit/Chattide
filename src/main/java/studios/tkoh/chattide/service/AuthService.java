package studios.tkoh.chattide.service;

import studios.tkoh.chattide.dto.request.UsuarioRequest;
import studios.tkoh.chattide.dto.request.auth.ChangePasswordRequest;
import studios.tkoh.chattide.dto.request.auth.LoginRequest;
import studios.tkoh.chattide.dto.request.auth.RefreshTokenRequest;
import studios.tkoh.chattide.dto.request.auth.ResetPasswordRequest;
import studios.tkoh.chattide.dto.response.auth.AuthResponse;

/**
 *
 * @author Studios TKOH!
 */
public interface AuthService {

    void registrarUsuario(UsuarioRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void enviarOtpRecuperacion(String correo);

    void restablecerPassword(ResetPasswordRequest request); // Usa OTP

    void cambiarPassword(ChangePasswordRequest request, Long usuarioId);
}
