package org.example.microservice.services.contractgame;

import dto.minigames.contractgame.SendResultContractToKafkaDto;
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

public class ParsingContractEventService {

    private final OutboxMiniGameRepository outboxMiniGameRepository;

    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedDelay = 3000)
    public void parseOutboxHandle() {
        List<OutboxMiniGame> processedObjects = outboxMiniGameRepository.findAllForProcessing("CONTRACT_GAME", List.of("USER_USED_CONTRACT"));

        if (processedObjects == null || processedObjects.isEmpty()) {
            return;
        }

        for (OutboxMiniGame outbox : processedObjects) {
            try {
                SendResultContractToKafkaDto event = objectMapper.readValue(
                        outbox.getPayload(),
                        SendResultContractToKafkaDto.class
                );

                sendResponseToKafka(event, outbox);

            } catch (Exception e) {
                log.error("Ошибка при обработке outbox записи с ID: {}", outbox.getId(), e);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendResponseToKafka(SendResultContractToKafkaDto event, OutboxMiniGame outbox) throws ExecutionException, InterruptedException {

        kafkaTemplate.send("won-skin-contract-response-topic", String.valueOf(event.userId()), event).get();
        outboxMiniGameRepository.delete(outbox);

        log.info("Результат контракта успешно отправлен в брокер для UserId: {}; SessionId: {}", event.userId(), event.sessionId());
    }

}
