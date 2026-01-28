package studios.tkoh.chattide.service.impl;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.chattide.dto.request.UsuarioRequest;
import studios.tkoh.chattide.dto.request.auth.ChangePasswordRequest;
import studios.tkoh.chattide.dto.request.auth.LoginRequest;
import studios.tkoh.chattide.dto.request.auth.RefreshTokenRequest;
import studios.tkoh.chattide.dto.request.auth.ResetPasswordRequest;
import studios.tkoh.chattide.dto.response.auth.AuthResponse;
import studios.tkoh.chattide.exception.ChattideException;
import studios.tkoh.chattide.exception.ResourceNotFoundException;
import studios.tkoh.chattide.mapper.UsuarioMapper;
import studios.tkoh.chattide.model.PasswordResetToken;
import studios.tkoh.chattide.model.RefreshToken;
import studios.tkoh.chattide.model.Usuario;
import studios.tkoh.chattide.model.enums.AppRole;
import studios.tkoh.chattide.repository.PasswordResetTokenRepository;
import studios.tkoh.chattide.repository.RefreshTokenRepository;
import studios.tkoh.chattide.repository.UsuarioRepository;
import studios.tkoh.chattide.security.JwtService;
import studios.tkoh.chattide.service.AuthService;
import studios.tkoh.chattide.service.EmailService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final EmailService emailService;

    @Override
    @Transactional
    public void registrarUsuario(UsuarioRequest request) {
        if (usuarioRepository.existsByCorreo(request.correo())) {
            throw new ChattideException("El correo ya está registrado");
        }

        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setPassword(passwordEncoder.encode(request.password()));
        // Asignar rol por defecto
        usuario.getRoles().add(AppRole.ROLE_USER);

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

        return generarAuthResponse(usuario);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new ChattideException("Refresh token no encontrado"));

        if (refreshToken.isRevocado() || refreshToken.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new ChattideException("Refresh token expirado o revocado");
        }

        Usuario usuario = refreshToken.getUsuario();

        // Revocamos el token usado (rotación de tokens para más seguridad)
        refreshToken.setRevocado(true);
        refreshTokenRepository.save(refreshToken);

        return generarAuthResponse(usuario);
    }

    @Override
    @Transactional
    public void enviarOtpRecuperacion(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "correo", correo));

        // Limpiar tokens anteriores si existen
        if (usuario.getPasswordResetToken() != null) {
            passwordResetTokenRepository.delete(usuario.getPasswordResetToken());
            usuario.setPasswordResetToken(null);
            usuarioRepository.saveAndFlush(usuario);
        }

        // Generar OTP de 6 dígitos
        String otp = String.format("%06d", new Random().nextInt(999999));

        PasswordResetToken token = PasswordResetToken.builder()
                .token(otp) // Guardamos el OTP como token
                .usuario(usuario)
                .fechaExpiracion(LocalDateTime.now().plusMinutes(15))
                .build();

        passwordResetTokenRepository.save(token);

        // Enviar Email
        emailService.enviarOtpRecuperacion(usuario.getCorreo(), otp);
    }

    @Override
    @Transactional
    public void restablecerPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.token())
                .orElseThrow(() -> new ChattideException("Código OTP inválido"));

        if (resetToken.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new ChattideException("El código OTP ha expirado");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setPassword(passwordEncoder.encode(request.newPassword()));
        usuarioRepository.save(usuario);

        // Consumir el token
        passwordResetTokenRepository.delete(resetToken);
    }

    @Override
    @Transactional
    public void cambiarPassword(ChangePasswordRequest request, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        if (!passwordEncoder.matches(request.currentPassword(), usuario.getPassword())) {
            throw new ChattideException("La contraseña actual es incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(request.newPassword()));
        usuarioRepository.save(usuario);
    }

    // Helper para generar tokens
    private AuthResponse generarAuthResponse(Usuario usuario) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getCorreo());
        String accessToken = jwtService.generateToken(userDetails);

        String refreshTokenString = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenString)
                .usuario(usuario)
                .fechaExpiracion(LocalDateTime.now().plusDays(7))
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
