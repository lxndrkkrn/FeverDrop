package org.example.microservice.services.upgradegame;

import dto.minigames.upgradegame.SendResultUpgradeToKafkaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.entities.outboxes.OutboxMiniGame;
import org.example.microservice.repositories.outboxes.OutboxMiniGameRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor

public class ParsingUpgradeEventService {

    private final OutboxMiniGameRepository outboxMiniGameRepository;

    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedDelay = 3000)
    public void parseOutboxHandle() {
        List<OutboxMiniGame> processedObjects = outboxMiniGameRepository.findAllForProcessing("UPGRADE_GAME", List.of("USER_WIN_SKIN_UPGRADE", "USER_LOSE_SKIN_UPGRADE"));

        if (processedObjects == null || processedObjects.isEmpty()) {
            return;
        }

        for (OutboxMiniGame outbox : processedObjects) {
            try {
                SendResultUpgradeToKafkaDto event = objectMapper.readValue(
                        outbox.getPayload(),
                        SendResultUpgradeToKafkaDto.class
                );

                sendResponseToKafka(event, outbox);

            } catch (Exception e) {
                log.error("Ошибка при обработке outbox записи с ID: {}", outbox.getId(), e);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendResponseToKafka(SendResultUpgradeToKafkaDto event, OutboxMiniGame outbox) throws ExecutionException, InterruptedException {

        kafkaTemplate.send("won-skin-upgrade-response-topic", String.valueOf(event.userId()), event).get();
        outboxMiniGameRepository.delete(outbox);

        log.info("Результат апгрейда успешно отправлен в брокер для UserId: {}; SessionId: {}", event.userId(), event.sessionId());
    }
}
