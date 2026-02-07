package studios.tkoh.chattide.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 *
 * @author Studios TKOH!
 */
public record UsuarioRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 50)
        String nombre,
        @NotBlank(message = "El apellido es obligatorio")
        @Size(min = 2, max = 50)
        String apellido,
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Formato de correo inválido")
        String correo,
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password,
        // Campos opcionales iniciales
        @Size(max = 500)
        String biografia,
        @Past(message = "La fecha de nacimiento debe ser en el pasado")
        LocalDate fechaNacimiento) {

}
