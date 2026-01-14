package studios.tkoh.chattide.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import studios.tkoh.chattide.dto.request.GrupoRequest;
import studios.tkoh.chattide.dto.response.GrupoResponse;
import studios.tkoh.chattide.model.Grupo;

/**
 *
 * @author Studios TKOH!
 */
@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface GrupoMapper {

    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "creador", source = "creador")
    @Mapping(target = "cantidadMiembros", expression = "java(grupo.getMiembros() != null ? grupo.getMiembros().size() : 0)")
    GrupoResponse toResponse(Grupo grupo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "miembros", ignore = true)
    @Mapping(target = "creador.id", source = "creadorId") // Mapea ID a la entidad Usuario
    Grupo toEntity(GrupoRequest request);
}
