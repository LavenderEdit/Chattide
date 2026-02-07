package studios.tkoh.chattide.dto.request;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 *
 * @author Studios TKOH!
 */
public record UsuarioUpdateRequest(
        @Size(min = 2, max = 50)
        String nombre,
        @Size(min = 2, max = 50)
        String apellido,
        @Size(max = 500)
        String biografia,
        @Size(max = 100)
        String ubicacion,
        @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$", message = "URL inválida")
        String sitioWeb,
        @Size(max = 20)
        String telefono,
        @Past
        LocalDate fechaNacimiento) {

}
