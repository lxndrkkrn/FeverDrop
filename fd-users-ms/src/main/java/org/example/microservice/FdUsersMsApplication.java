package org.example.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "org.example")

@Import(value = {
        org.example.kafka.ProducerConfigKafka.class,
        org.example.kafka.ConsumerConfigKafka.class,
        org.example.kafka.DltConfig.class,
        org.example.rabbitMq.RabbitMqConfig.class,
        org.example.security.SecurityConfig.class,
        org.example.webSocket.WebSocketConfig.class
})

public class FdUsersMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FdUsersMsApplication.class, args);
    }

}
