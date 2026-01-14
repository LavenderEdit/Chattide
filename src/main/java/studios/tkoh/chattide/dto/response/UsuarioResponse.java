package studios.tkoh.chattide.dto.response;

import java.time.LocalDateTime;

/**
 *
 * @author Studios TKOH!
 */
public record UsuarioResponse(
        Long id,
        String nombre,
        String apellido,
        String correo,
        String fotoPerfil,
        Boolean activo,
        LocalDateTime fechaRegistro) {

}
