package org.example.microservice.services.casegame;

import dto.minigames.casegame.SendWonSkinToKafkaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.entities.outboxes.OutboxMiniGame;
import org.example.microservice.repositories.outboxes.OutboxMiniGameRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor

public class ParsingCaseEventService {

    private final OutboxMiniGameRepository outboxMiniGameRepository;

    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void parseOutboxHandle() {
        List<OutboxMiniGame> processedObjects = outboxMiniGameRepository.findAllForProcessing();

        if (processedObjects == null || processedObjects.isEmpty()) {
            return;
        }

        for (OutboxMiniGame outbox : processedObjects) {
            try {
                SendWonSkinToKafkaDto event = objectMapper.readValue(
                        outbox.getPayload(),
                        SendWonSkinToKafkaDto.class
                );

                kafkaTemplate.send("send-won-skin-response-topic", String.valueOf(event.userId()), event).get();
                log.info("Скин успешно отправлен в брокер для userId: {}", event.userId());

                outboxMiniGameRepository.delete(outbox);

            } catch (Exception e) {
                log.error("Ошибка при обработке outbox записи с ID: {}", outbox.getId(), e);
            }
        }
    }

}
