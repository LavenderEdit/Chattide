package studios.tkoh.chattide.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.chattide.dto.request.ComentarioRequest;
import studios.tkoh.chattide.dto.response.ComentarioResponse;
import studios.tkoh.chattide.exception.ChattideException;
import studios.tkoh.chattide.exception.ResourceNotFoundException;
import studios.tkoh.chattide.mapper.ComentarioMapper;
import studios.tkoh.chattide.model.Comentario;
import studios.tkoh.chattide.repository.ComentarioRepository;
import studios.tkoh.chattide.service.ComentarioService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final ComentarioMapper comentarioMapper;

    @Override
    @Transactional
    public ComentarioResponse agregarComentario(ComentarioRequest request) {
        Comentario comentario = comentarioMapper.toEntity(request);
        return comentarioMapper.toResponse(comentarioRepository.save(comentario));
    }

    @Override
    @Transactional
    public void eliminarComentario(Long comentarioId, Long usuarioSolicitanteId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario", "id", comentarioId));

        if (!comentario.getUsuario().getId().equals(usuarioSolicitanteId)) {
            throw new ChattideException("No puedes eliminar este comentario");
        }
        comentarioRepository.delete(comentario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComentarioResponse> obtenerComentariosDePost(Long publicacionId) {
        return comentarioMapper.toResponseList(comentarioRepository.findByPublicacionIdOrderByFechaCreacionAsc(publicacionId));
    }
}
