package studios.tkoh.chattide.dto.response;

import java.time.LocalDateTime;

/**
 *
 * @author Studios TKOH!
 */
public record GrupoResponse(
        Long id,
        String nombre,
        String descripcion,
        LocalDateTime fechaCreacion,
        UsuarioResponse creador,
        int cantidadMiembros) {

}
