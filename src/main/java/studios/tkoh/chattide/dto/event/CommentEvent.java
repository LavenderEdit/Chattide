package studios.tkoh.chattide.dto.event;

import java.time.LocalDateTime;

/**
 *
 * @author Studios TKOH!
 */
public record CommentEvent(
        Long commentId,
        Long postId,
        Long authorId,
        String authorName,
        String postOwnerEmail,
        String content,
        LocalDateTime timestamp) {

}
