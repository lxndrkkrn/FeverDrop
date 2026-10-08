package org.example.feverdrop;

import org.example.rabbitMq.RabbitMqConfig;
import org.example.webSocket.WebSocketConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "org.example")

@Import(value = {
        org.example.kafka.ProducerConfigKafka.class,
        org.example.kafka.ConsumerConfigKafka.class,
        org.example.kafka.DltConfig.class,
        RabbitMqConfig.class,
        org.example.security.SecurityConfig.class,
        WebSocketConfig.class
})
public class FeverDropApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeverDropApplication.class, args);
    }

}
