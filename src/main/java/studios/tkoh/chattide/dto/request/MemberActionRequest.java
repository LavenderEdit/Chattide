package studios.tkoh.chattide.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Studios TKOH!
 */
public record MemberActionRequest(
        @NotNull(message = "El ID del grupo es obligatorio")
        Long grupoId,
        @NotNull(message = "El ID del usuario objetivo es obligatorio")
        Long targetUsuarioId,
        String nuevoRol // Opcional: "ADMIN", "MIEMBRO" (solo para cambios de rol)
        ) {

}
