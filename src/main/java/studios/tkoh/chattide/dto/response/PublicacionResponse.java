package studios.tkoh.chattide.dto.response;

import java.time.LocalDateTime;

/**
 *
 * @author Studios TKOH!
 */
public record PublicacionResponse(
        Long id,
        String titulo,
        String contenido,
        String imagenUrl,
        LocalDateTime fechaPublicacion,
        UsuarioResponse autor,
        Long grupoId,
        String grupoNombre,
        int likesCount,
        int comentariosCount) {

}
