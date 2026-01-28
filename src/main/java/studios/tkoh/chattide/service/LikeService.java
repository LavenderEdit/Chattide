package studios.tkoh.chattide.service;

import studios.tkoh.chattide.model.enums.EntityType;

/**
 *
 * @author Studios TKOH!
 */
public interface LikeService {

    void toggleLike(Long entityId, EntityType entityType, Long usuarioId);
}
