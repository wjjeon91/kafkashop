package kafkashop.portal.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageConsumer {

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.consumer.topic}"
    )
    public void consumeMessage(ConsumerRecord<String, String> record) {
        String key = record.key();
        String value = record.value();
        System.out.println("Received message: " + key + " -> " + value);
        // Add your custom message processing logic here
    }
}
