package studios.tkoh.chattide.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import studios.tkoh.chattide.dto.event.CommentEvent;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyPostOwner(CommentEvent event) {
        String destination = "/queue/notifications";
        // Using convertAndSendToUser allows us to target a specific user by their email (principal name)
        messagingTemplate.convertAndSendToUser(
                event.postOwnerEmail(),
                destination,
                event
        );
    }
}
