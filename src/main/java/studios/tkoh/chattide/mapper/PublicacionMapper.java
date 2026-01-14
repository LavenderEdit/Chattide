package studios.tkoh.chattide.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import studios.tkoh.chattide.dto.request.PublicacionRequest;
import studios.tkoh.chattide.dto.response.PublicacionResponse;
import studios.tkoh.chattide.model.Publicacion;

/**
 *
 * @author Studios TKOH!
 */
@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface PublicacionMapper {

    @Mapping(target = "fechaPublicacion", source = "fechaRegistro")
    @Mapping(target = "autor", source = "usuario")
    @Mapping(target = "grupoId", source = "grupo.id")
    @Mapping(target = "grupoNombre", source = "grupo.nombre")
    @Mapping(target = "likesCount", expression = "java(publicacion.getLikes() != null ? publicacion.getLikes().size() : 0)")
    @Mapping(target = "comentariosCount", expression = "java(publicacion.getComentarios() != null ? publicacion.getComentarios().size() : 0)")
    PublicacionResponse toResponse(Publicacion publicacion);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "comentarios", ignore = true)
    @Mapping(target = "usuario.id", source = "usuarioId")
    @Mapping(target = "grupo.id", source = "grupoId")
    Publicacion toEntity(PublicacionRequest request);
}
