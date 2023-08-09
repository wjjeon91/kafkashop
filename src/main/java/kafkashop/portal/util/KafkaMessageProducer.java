package kafkashop.portal.util;

import java.util.Properties;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/kafka")
public class KafkaMessageProducer {

    @GetMapping("/kafkaTest")
//    @ResponseBody
    public String kafkaTest(){
        // 카프카 클러스터의 주소와 설정을 정의합니다.
        String bootstrapServers = "localhost:9092";
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // KafkaProducer 객체를 생성합니다.
        Producer<String, String> producer = new KafkaProducer<>(props);

        // 메시지를 보낼 토픽 이름을 지정합니다.
//        String topic = "dev-topic";
        String topic = "first_topic";

        try {
            // 메시지를 생성하고, 해당 토픽으로 보냅니다.
            for (int i = 0; i < 10; i++) {
                String key = "key_" + i;
                String value = "Message " + (char)(i+65);
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
                producer.send(record);
                System.out.println("Sent message: " + key + " -> " + value);
            }
            System.out.println("Messages sent successfully!");
//            return "Messages sent successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to send messages!");
//            return "Failed to send messages!";
        } finally {
            // 프로듀서를 종료합니다.
            producer.close();
            return "redirect:/";
        }
    }
}
