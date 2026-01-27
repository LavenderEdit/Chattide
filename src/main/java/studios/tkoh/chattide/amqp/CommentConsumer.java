package studios.tkoh.chattide.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import studios.tkoh.chattide.dto.event.CommentEvent;
import studios.tkoh.chattide.service.NotificationService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CommentConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${chattide.amqp.queue}")
    public void handleCommentNotification(CommentEvent event) {
        log.info("Processing AMQP event: User {} commented on post {}",
                event.authorName(), event.postId());

        // Push the real-time notification via WebSocket
        notificationService.notifyPostOwner(event);

        log.info("WebSocket notification sent to {}", event.postOwnerEmail());
    }
}
