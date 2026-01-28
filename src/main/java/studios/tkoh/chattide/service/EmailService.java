package studios.tkoh.chattide.service;

/**
 *
 * @author Studios TKOH!
 */
public interface EmailService {

    void enviarCorreo(String destinatario, String asunto, String cuerpo);

    void enviarOtpRecuperacion(String destinatario, String otp);
}
