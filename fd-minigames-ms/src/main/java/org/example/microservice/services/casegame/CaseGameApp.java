package org.example.microservice.services.casegame;

import dto.minigames.casegame.SendWonSkinToKafkaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.domain.server.ServiceUnavailable;
import org.example.microservice.entities.SkinCard;
import org.example.microservice.entities.games.CaseGame;
import org.example.microservice.entities.outboxes.Outbox;
import org.example.microservice.repositories.outboxes.OutboxRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor

public class CaseGameApp {

    private final CaseGameHandle caseGameHandle;
    private final CaseGameService caseGameService;

    private final OutboxRepository outboxRepository;

    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public void openCaseHandle(Long userId, Long caseId, BigDecimal userBalance) {
        log.info("Игрок {} открывает кейс {}", userId, caseId);

        try {
            CaseGame case_ = caseGameService.findCaseById(caseId);

            if (userBalance.compareTo(case_.getPrice()) < 0) {
                log.warn("У игрока {} не хватает баланса для кейса {}", userId, caseId);
                throw new org.example.exceptions.domain.client.PaymentRequired("Не достаточно денег на балансе");
            }

            SkinCard wonSkin = caseGameHandle.getWonSkin(case_.getId());

            sendWonSkinToKafka(wonSkin, userId);

        } catch (Exception e) {
            log.error("Ошибка при открытии кейса, UserId: {}, CaseId: {}", userId, caseId, e);
            throw new ServiceUnavailable("Сервис временно недоступен");
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void sendWonSkinToKafka(SkinCard wonSkin, Long userId) {
        log.info("Отправка выиграного из кейса скина в кафку (FullName: {}, UserId:{})", wonSkin.getFullName(), userId);

        SendWonSkinToKafkaDto event = new SendWonSkinToKafkaDto(
                wonSkin.getFullName(),
                userId
        );

        try {
            Outbox outbox = new Outbox();

            outbox.setAggregateType("CASE_GAME");
            outbox.setEventType("USER_WON_SKIN");
            outbox.setPayload(objectMapper.writeValueAsString(event));

            outboxRepository.save(outbox);
            log.info("Событие выигрыша зафиксировано в таблице Outbox");

        } catch (Exception e) {
            throw new RuntimeException("Критическая ошибка записи события выигрыша в Outbox", e);
        }

//        kafkaTemplate.send("send-won-skin-response-topic", String.valueOf(userId), event);
//        log.info("Скин отправлен в брокер");
//        ПОЗЖЕ
    }

}
