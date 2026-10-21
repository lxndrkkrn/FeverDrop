package org.example.microservice.services.upgradegame;

import dto.minigames.upgradegame.SendResultUpgradeToKafkaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.domain.client.NotFound;
import org.example.exceptions.domain.server.ServiceUnavailable;
import org.example.microservice.dtos.UpgradeResponseDto;
import org.example.microservice.entities.SkinCard;
import org.example.microservice.entities.outboxes.OutboxMiniGame;
import org.example.microservice.repositories.SkinCardRepository;
import org.example.microservice.repositories.outboxes.OutboxMiniGameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor

public class UpgradeGameApp {

    private final SkinCardRepository skinCardRepository;
    private final UpgradeGameHandle upgradeGameHandle;

    private final OutboxMiniGameRepository outboxMiniGameRepository;

    private final ObjectMapper objectMapper;

    @Transactional
    public void upgradeSkin(Long userId, Long userSkinId, Long marketSkinId, Long sessionId) {
        log.info("Игрок {} апгрейдит свой скин {} до {}", userId, userSkinId, marketSkinId);

        SkinCard userSkin = skinCardRepository.findById(userSkinId)
                .orElseThrow(() -> new NotFound("Скин не найден"));

        SkinCard marketSkin = skinCardRepository.findById(marketSkinId)
                .orElseThrow(() -> new NotFound("Скин не найден"));

        UpgradeResponseDto response = upgradeGameHandle.getWonSkinOrNull(userSkin, marketSkin);

        if (response.result() == null || response.result().isEmpty()) {
            throw new ServiceUnavailable("Сервис временно недоступен: результат апгрейда неизвестен");
        }

        saveEventToOutbox(userId, response.result(), sessionId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveEventToOutbox(Long userId, String result, Long sessionId) {
        log.info("Отправка результата {} апгрейда у игрока {} в кафку (Сессия: {})", result, userId, sessionId);

        SendResultUpgradeToKafkaDto event = new SendResultUpgradeToKafkaDto(
                userId,
                result,
                sessionId
        );

        try {
            OutboxMiniGame outbox = new OutboxMiniGame();

            outbox.setAggregateType("UPGRADE_GAME");

            if (result.equals("WIN")) {
                outbox.setEventType("USER_WIN_SKIN_UPGRADE");
            } else {
                outbox.setEventType("USER_LOSE_SKIN_UPGRADE");
            }

            outbox.setPayload(objectMapper.writeValueAsString(event));

            outboxMiniGameRepository.save(outbox);
            log.info("Событие и результат апгрейда записанны в Outbox");

        } catch (Exception e) {
            throw new RuntimeException("Критическая ошибка записи события и результата в Outbox", e);
        }
    }

}
