package org.example.microservice.services.casegame;

import dto.minigames.UserOpenedCaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.domain.client.BadRequest;
import org.example.exceptions.domain.client.PaymentRequired;
import org.example.microservice.entities.CaseGame;
import org.example.microservice.entities.SkinCard;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor

public class CaseGameApp {

    private final CaseGameService caseGameService;

    private final KafkaTemplate<String, Object> kafkaTemplate;



//    @Transactional
//    public String openCaseHandle(Long userId, Long caseId, BigDecimal userBalance) {
//
//        CaseGame case_ = caseGameService.findCaseById(caseId);
//
//        if (userBalance.compareTo(case_.getPrice()) <= 0) {
//            log.warn("У игрока {} не хватает баланса для кейса {}", userId, caseId);
//            throw new PaymentRequired("Не достаточно денег на балансе");
//        }
//
//
//
//    }

}
