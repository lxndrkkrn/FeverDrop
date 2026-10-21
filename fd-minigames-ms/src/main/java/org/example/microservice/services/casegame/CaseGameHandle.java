package org.example.microservice.services.casegame;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.components.systemwallet.SystemWalletCache;
import org.example.microservice.entities.SkinCard;
import org.example.microservice.entities.games.CaseGame;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor

public class CaseGameHandle {

    private final CaseGameService caseGameService;

    private final SystemWalletCache systemWalletCache;

    private final SecureRandom secureRandom = new SecureRandom();

//    private void rollbackUpdate() {
//
//    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public SkinCard getWonSkin(Long caseId) {

        BigDecimal totalBalance = systemWalletCache.getTotalBalance();
        BigDecimal prizePool = systemWalletCache.getPrizePool();

        CaseGame case_ = caseGameService.findCaseById(caseId);

        if (case_ == null) {
            throw new org.example.exceptions.domain.client.BadRequest("Такого кейса не существует");
        }

        if (totalBalance == null || prizePool == null) {
            throw new org.example.exceptions.domain.server.ServiceUnavailable("Сервис временно не доступен: Баланса проекта не существует");
        }

        if (totalBalance.compareTo(prizePool) < 0) {
            throw new org.example.exceptions.domain.server.ServiceUnavailable("Сервис временно не доступен: Баланс проекта меньше призового фонда");
        }

        if (case_.getSkins().isEmpty()) {
            throw new org.example.exceptions.domain.server.ServiceUnavailable("Сервис временно недоступен: В кейсе нет ни одного скина");
        }

        List<SkinCard> activeSkins = case_.getSkins().stream()
                .filter(s -> s.getMarketPrice() != null && s.getMarketPrice().compareTo(prizePool) <= 0)
                .toList();

        if (activeSkins.isEmpty()) {
            log.warn("В кейсе нет ни одного скина дешевле призового пула или у них невалидная цена");
        }

        return chooseSkinByWeightedRandom(activeSkins);
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public SkinCard chooseSkinByWeightedRandom(List<SkinCard> activeSkins) {
        if (activeSkins.size() == 1) {
            return activeSkins.getFirst();
        }

        BigDecimal totalWeight = BigDecimal.ZERO;
        for (SkinCard card : activeSkins) {

            BigDecimal price = card.getMarketPrice().compareTo(BigDecimal.ZERO) == 0
                    ? new BigDecimal("0.01")
                    : card.getMarketPrice();

            BigDecimal weight = BigDecimal.ONE.divide(price, 10, RoundingMode.HALF_UP);
            totalWeight = totalWeight.add(weight);
        }

        double randomValue = secureRandom.nextDouble();
        BigDecimal shot = totalWeight.multiply(BigDecimal.valueOf(randomValue));

        log.info("Суммарный вес кейса: {}. Значение случайного выстрела: {}", totalWeight, shot);

        BigDecimal currentSum = BigDecimal.ZERO;
        for (SkinCard skin : activeSkins) {
            BigDecimal price = skin.getMarketPrice().compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal("0.01") : skin.getMarketPrice();
            BigDecimal weight = BigDecimal.ONE.divide(price, 10, RoundingMode.HALF_UP);

            currentSum = currentSum.add(weight);

            if (currentSum.compareTo(shot) >= 0) {
                log.info("Победил скин: {} (Цена: {})", skin.getFullName(), skin.getMarketPrice());
                return skin;
            }
        }

        return activeSkins.getLast();
    }

}
