package org.example.microservice.services.contractgame;

import dto.minigames.casegame.SendWonSkinToKafkaDto;
import dto.minigames.contractgame.SendResultContractToKafkaDto;
import dto.minigames.upgradegame.SendResultUpgradeToKafkaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.domain.client.BadRequest;
import org.example.microservice.entities.SkinCard;
import org.example.microservice.entities.outboxes.OutboxMiniGame;
import org.example.microservice.repositories.SkinCardRepository;
import org.example.microservice.repositories.outboxes.OutboxMiniGameRepository;
import org.example.microservice.services.upgradegame.UpgradeGameHandle;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor

public class ContractGameApp {

    private final SkinCardRepository skinCardRepository;
    private final ContractGameHandle contractGameHandle;

    private final OutboxMiniGameRepository outboxMiniGameRepository;

    private final ObjectMapper objectMapper;

    @Transactional
    public void useOfAContract(Long userId, BigDecimal allPrice, Long sessionId) {
        log.info("Игрок {} подписывает контракт на сумму {}", userId, allPrice);

        SkinCard wonSkin = contractGameHandle.getWonSkin(allPrice);

        if (wonSkin == null) {
            throw new BadRequest("Выигранный скин может быть пустым");
        }

        SendWonSkinToKafkaDto skinDto = new SendWonSkinToKafkaDto(
                wonSkin.getFullName(),
                wonSkin.getMarketPrice(),
                wonSkin.getRarity(),
                wonSkin.getImageUrl(),
                null
        );

        saveEventToOutbox(skinDto, userId, sessionId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveEventToOutbox(SendWonSkinToKafkaDto skin, Long userId, Long sessionId) {
        log.info("Отправка результата апгрейда у игрока в кафку сессия");

        SendResultContractToKafkaDto event = new SendResultContractToKafkaDto(
                sessionId,
                skin,
                userId
        );

        try {
            OutboxMiniGame outbox = new OutboxMiniGame();

            outbox.setAggregateType("CONTRACT_GAME");
            outbox.setEventType("USER_USED_CONTRACT");

            outbox.setPayload(objectMapper.writeValueAsString(event));

            outboxMiniGameRepository.save(outbox);
            log.info("Событие и результат апгрейда записанны в Outbox");

        } catch (Exception e) {
            throw new RuntimeException("Критическая ошибка записи события и результата в Outbox", e);
        }
    }

}
