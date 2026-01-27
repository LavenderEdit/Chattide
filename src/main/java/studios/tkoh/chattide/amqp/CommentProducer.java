package studios.tkoh.chattide.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import studios.tkoh.chattide.dto.event.CommentEvent;

/**
 *
 * @author Studios TKOH!
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CommentProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${chattide.amqp.exchange}")
    private String exchange;

    @Value("${chattide.amqp.routing-key}")
    private String routingKey;

    public void sendCommentNotification(CommentEvent event) {
        log.info("Sending comment event to RabbitMQ for comment ID: {}", event.commentId());
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
