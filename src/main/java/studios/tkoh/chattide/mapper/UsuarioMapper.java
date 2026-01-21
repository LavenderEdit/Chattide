package studios.tkoh.chattide.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import studios.tkoh.chattide.dto.request.UsuarioRequest;
import studios.tkoh.chattide.dto.request.UsuarioUpdateRequest;
import studios.tkoh.chattide.dto.response.UsuarioResponse;
import studios.tkoh.chattide.model.Usuario;

/**
 *
 * @author Studios TKOH!
 */
@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "fechaRegistro", source = "fechaRegistro")
    UsuarioResponse toResponse(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "membresias", ignore = true)
    @Mapping(target = "refreshTokens", ignore = true)
    @Mapping(target = "passwordResetToken", ignore = true)
    Usuario toEntity(UsuarioRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "correo", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "membresias", ignore = true)
    @Mapping(target = "refreshTokens", ignore = true)
    @Mapping(target = "passwordResetToken", ignore = true)
    void updateEntityFromRequest(UsuarioUpdateRequest request, @MappingTarget Usuario entity);
}
