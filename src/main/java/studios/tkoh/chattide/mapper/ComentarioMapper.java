package studios.tkoh.chattide.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import studios.tkoh.chattide.dto.request.ComentarioRequest;
import studios.tkoh.chattide.dto.response.ComentarioResponse;
import studios.tkoh.chattide.model.Comentario;

/**
 *
 * @author Studios TKOH!
 */
@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface ComentarioMapper {

    @Mapping(target = "fechaComentario", source = "fechaRegistro")
    @Mapping(target = "autor", source = "usuario")
    ComentarioResponse toResponse(Comentario comentario);

    List<ComentarioResponse> toResponseList(List<Comentario> comentarios);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "usuario.id", source = "usuarioId")
    @Mapping(target = "publicacion.id", source = "publicacionId")
    Comentario toEntity(ComentarioRequest request);
}
