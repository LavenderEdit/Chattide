package studios.tkoh.chattide.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.chattide.dto.request.UsuarioRequest;
import studios.tkoh.chattide.dto.request.auth.LoginRequest;
import studios.tkoh.chattide.dto.response.auth.AuthResponse;
import studios.tkoh.chattide.exception.ChattideException;
import studios.tkoh.chattide.mapper.UsuarioMapper;
import studios.tkoh.chattide.model.Usuario;
import studios.tkoh.chattide.repository.UsuarioRepository;
import studios.tkoh.chattide.service.AuthService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    // private final JwtService jwtService; // Se inyectará cuando se configure JWT

    @Override
    @Transactional
    public void registrarUsuario(UsuarioRequest request) {
        if (usuarioRepository.existsByCorreo(request.correo())) {
            throw new ChattideException("El correo ya está registrado");
        }

        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setPassword(passwordEncoder.encode(request.password()));

        // Asignar avatar por defecto si no viene
        if (usuario.getFotoPerfil() == null || usuario.getFotoPerfil().isEmpty()) {
            usuario.setFotoPerfil("/images/Usuario/DefaultUserAvatar.webp");
        }

        usuarioRepository.save(usuario);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.correo())
                .orElseThrow(() -> new ChattideException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new ChattideException("Credenciales inválidas");
        }

        // TODO: Generar Token real
        String accessToken = "mock-jwt-token";
        String refreshToken = "mock-refresh-token";

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                3600L,
                usuarioMapper.toResponse(usuario)
        );
    }
}
