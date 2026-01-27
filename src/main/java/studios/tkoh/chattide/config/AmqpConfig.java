package studios.tkoh.chattide.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Studios TKOH!
 */
@Configuration
public class AmqpConfig {

    @Value("${chattide.amqp.exchange}")
    private String exchange;

    @Value("${chattide.amqp.queue}")
    private String queue;

    @Value("${chattide.amqp.routing-key}")
    private String routingKey;

    @Bean
    public DirectExchange commentExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue notificationQueue() {
        return (Queue) QueueBuilder.durable(queue).build();
    }

    @Bean
    public Binding commentBinding(Queue notificationQueue, DirectExchange commentExchange) {
        return BindingBuilder.bind(notificationQueue).to(commentExchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
