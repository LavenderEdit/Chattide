package studios.tkoh.chattide.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Studios TKOH!
 */
public record LikeRequest(
        @NotNull
        Long entityId,
        @NotNull
        String entityType // "POST" o "COMMENT"
        ) {

}
