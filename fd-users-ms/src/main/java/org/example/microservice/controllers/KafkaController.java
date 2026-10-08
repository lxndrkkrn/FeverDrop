package org.example.microservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.dtos.TestKafkaDto;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka-test")
@Validated
@Slf4j
@RequiredArgsConstructor


public class KafkaController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/send")
    public ResponseEntity<?> sendKafka(@RequestBody TestKafkaDto dto) {
        log.info("Тест отправки сообщения в кафку...");

        String topic = "test-kafka-topic";
        String key = "key";

        kafkaTemplate.send(topic, key, dto);

        log.info("(1) - Сообщение в кафку улетело");
        return ResponseEntity.ok(200);
    }

}
