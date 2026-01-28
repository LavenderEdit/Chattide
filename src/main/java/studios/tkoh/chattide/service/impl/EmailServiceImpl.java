package studios.tkoh.chattide.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import studios.tkoh.chattide.service.EmailService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpo, true); // true = html

            mailSender.send(message);
            log.info("Correo enviado exitosamente a: {}", destinatario);
        } catch (MessagingException e) {
            log.error("Error al enviar correo a {}: {}", destinatario, e.getMessage());
        }
    }

    @Override
    public void enviarOtpRecuperacion(String destinatario, String otp) {
        String asunto = "Chattide - Código de Recuperación";
        String cuerpo = String.format(
                "<div style='font-family: Arial, sans-serif; color: #333;'>"
                + "  <h2>Solicitud de restablecimiento de contraseña</h2>"
                + "  <p>Usa el siguiente código OTP para restablecer tu contraseña. Este código expira en 15 minutos.</p>"
                + "  <h1 style='color: #4A90E2; letter-spacing: 5px;'>%s</h1>"
                + "  <p>Si no solicitaste este cambio, ignora este correo.</p>"
                + "</div>", otp);

        enviarCorreo(destinatario, asunto, cuerpo);
    }
}
