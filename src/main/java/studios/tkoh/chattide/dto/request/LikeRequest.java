package studios.tkoh.chattide.dto.request;

import jakarta.validation.constraints.NotNull;
import studios.tkoh.chattide.model.enums.EntityType;

/**
 *
 * @author Studios TKOH!
 */
public record LikeRequest(
        @NotNull
        Long entityId,
        @NotNull
        EntityType entityType) {

}
