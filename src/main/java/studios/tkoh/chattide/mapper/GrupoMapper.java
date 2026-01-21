package studios.tkoh.chattide.mapper;

import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import studios.tkoh.chattide.dto.request.GrupoRequest;
import studios.tkoh.chattide.dto.response.GrupoResponse;
import studios.tkoh.chattide.model.Grupo;

/**
 *
 * @author Studios TKOH!
 */
@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface GrupoMapper {

    @Mapping(target = "fechaCreacion", source = "fechaRegistro")
    @Mapping(target = "creador", source = "creador")
    @Mapping(target = "cantidadMiembros", expression = "java(grupo.getMiembros() != null ? grupo.getMiembros().size() : 0)")
    GrupoResponse toResponse(Grupo grupo);

    List<GrupoResponse> toResponseList(List<Grupo> grupos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "miembros", ignore = true)
    @Mapping(target = "creador.id", source = "creadorId")
    Grupo toEntity(GrupoRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "creador", ignore = true)
    @Mapping(target = "miembros", ignore = true)
    void updateEntityFromRequest(GrupoRequest request, @MappingTarget Grupo entity);
}
