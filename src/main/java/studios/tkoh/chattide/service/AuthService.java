package studios.tkoh.chattide.service;

import studios.tkoh.chattide.dto.request.UsuarioRequest;
import studios.tkoh.chattide.dto.request.auth.LoginRequest;
import studios.tkoh.chattide.dto.response.auth.AuthResponse;

/**
 *
 * @author Studios TKOH!
 */
public interface AuthService {

    void registrarUsuario(UsuarioRequest request);

    AuthResponse login(LoginRequest request);
    // Aquí iran refresh token y recovery en la V2 completa
}
