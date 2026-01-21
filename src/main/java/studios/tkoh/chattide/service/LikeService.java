package studios.tkoh.chattide.service;

import studios.tkoh.chattide.dto.request.LikeRequest;

/**
 *
 * @author Studios TKOH!
 */
public interface LikeService {

    void toggleLike(LikeRequest request, Long usuarioId);
}
