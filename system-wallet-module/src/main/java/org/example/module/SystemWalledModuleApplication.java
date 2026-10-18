package org.example.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {
        "org.example.module"
})

@Import(value = {
        org.example.kafka.ProducerConfigKafka.class,
        org.example.kafka.ConsumerConfigKafka.class,
        org.example.kafka.DltConfig.class,
        org.example.rabbitMq.RabbitMqConfig.class,
        org.example.security.SecurityConfig.class,
        org.example.webSocket.WebSocketConfig.class
})

public class SystemWalledModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemWalledModuleApplication.class, args);
    }

}
