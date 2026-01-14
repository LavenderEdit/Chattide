package studios.tkoh.chattide.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import studios.tkoh.chattide.dto.request.UsuarioRequest;
import studios.tkoh.chattide.dto.response.UsuarioResponse;
import studios.tkoh.chattide.model.Usuario;

/**
 *
 * @author Studios TKOH!
 */
@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "fechaRegistro", source = "fechaRegistro") // Mapeo de BaseEntity
    UsuarioResponse toResponse(Usuario usuario);

    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "membresias", ignore = true)
    @Mapping(target = "refreshTokens", ignore = true)
    @Mapping(target = "passwordResetToken", ignore = true)
    Usuario toEntity(UsuarioRequest request);
}
