package org.example.microservice.services.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.dtos.TestKafkaDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor

public class TestKafkaService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "test-kafka-topic", groupId = "kafka-tests")
    public void kafkaListen(TestKafkaDto message) {
        log.info("Метод kafkaListen триггернули...");

        log.info("MESSAGE: String - {}, Integer - {}", message.string(), message.integer());
        log.info("Сообщение получено и распакованно!");
    }

}
