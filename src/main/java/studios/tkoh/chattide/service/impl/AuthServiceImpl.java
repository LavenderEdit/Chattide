package studios.tkoh.chattide.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.chattide.dto.request.UsuarioRequest;
import studios.tkoh.chattide.dto.request.auth.LoginRequest;
import studios.tkoh.chattide.dto.response.auth.AuthResponse;
import studios.tkoh.chattide.exception.ChattideException;
import studios.tkoh.chattide.mapper.UsuarioMapper;
import studios.tkoh.chattide.model.RefreshToken;
import studios.tkoh.chattide.model.Usuario;
import studios.tkoh.chattide.repository.RefreshTokenRepository;
import studios.tkoh.chattide.repository.UsuarioRepository;
import studios.tkoh.chattide.security.JwtService;
import studios.tkoh.chattide.service.AuthService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    @Transactional
    public void registrarUsuario(UsuarioRequest request) {
        if (usuarioRepository.existsByCorreo(request.correo())) {
            throw new ChattideException("El correo ya está registrado");
        }

        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setPassword(passwordEncoder.encode(request.password()));

        if (usuario.getFotoPerfil() == null || usuario.getFotoPerfil().isEmpty()) {
            usuario.setFotoPerfil("/images/Usuario/DefaultUserAvatar.webp");
        }

        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.correo())
                .orElseThrow(() -> new ChattideException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new ChattideException("Credenciales inválidas");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getCorreo());

        // Generate real Access Token
        String accessToken = jwtService.generateToken(userDetails);

        // Generate and save Refresh Token
        String refreshTokenString = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenString)
                .usuario(usuario)
                .fechaExpiracion(LocalDateTime.now().plusDays(7)) // Valid for 7 days
                .revocado(false)
                .build();

        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(
                accessToken,
                refreshTokenString,
                "Bearer",
                jwtService.getExpirationTime(),
                usuarioMapper.toResponse(usuario)
        );
    }
}
