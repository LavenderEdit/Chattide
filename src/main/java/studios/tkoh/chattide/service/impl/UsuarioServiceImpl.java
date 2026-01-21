package studios.tkoh.chattide.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.chattide.dto.request.BusquedaRequest;
import studios.tkoh.chattide.dto.request.UsuarioUpdateRequest;
import studios.tkoh.chattide.dto.response.UsuarioResponse;
import studios.tkoh.chattide.exception.ResourceNotFoundException;
import studios.tkoh.chattide.mapper.UsuarioMapper;
import studios.tkoh.chattide.model.Usuario;
import studios.tkoh.chattide.repository.UsuarioRepository;
import studios.tkoh.chattide.service.UsuarioService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPerfil(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponse actualizarPerfil(Long usuarioId, UsuarioUpdateRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        usuarioMapper.updateEntityFromRequest(request, usuario);

        Usuario actualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(actualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponse> buscarUsuarios(BusquedaRequest request, Pageable pageable) {
        Page<Usuario> paginaUsuarios = usuarioRepository.buscarUsuarios(request.query(), pageable);
        return paginaUsuarios.map(usuarioMapper::toResponse);
    }
}
