package org.example.microservice.services.contractgame;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.domain.client.NotFound;
import org.example.exceptions.domain.server.ServiceUnavailable;
import org.example.microservice.components.systemwallet.SystemWalletCache;
import org.example.microservice.entities.SkinCard;
import org.example.microservice.repositories.SkinCardRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

@Service
@Slf4j
@RequiredArgsConstructor

public class ContractGameHandle {

    private final SkinCardRepository skinCardRepository;

    private final SystemWalletCache systemWalletCache;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${contract.max:2")
    private Double maxMultiply;

    @Value("${contract.min:0.5")
    private Double minMultiply;

    @Transactional(propagation = Propagation.MANDATORY)
    public SkinCard getWonSkin(BigDecimal allPrice) {
        BigDecimal totalBalance = systemWalletCache.getTotalBalance();
        BigDecimal prizePool = systemWalletCache.getPrizePool();

        if (totalBalance == null || prizePool == null) {
            throw new org.example.exceptions.domain.server.ServiceUnavailable("Сервис временно недоступен: Баланс проекта не синхронизирован");
        }

        if (prizePool.compareTo(allPrice) <= 0) {
            throw new ServiceUnavailable("Сервис временно недоступен: в призовом пуле недостаточно баланса");
        }

        double randomValue = secureRandom.nextDouble(minMultiply, maxMultiply);
        BigDecimal randomShot = BigDecimal.valueOf(randomValue).setScale(4, RoundingMode.DOWN);

        log.info("Расчёт результата контракта. Итоговый множитель: {}", randomShot);

        BigDecimal wonSkinPrice = allPrice.multiply(randomShot);

        if (prizePool.compareTo(wonSkinPrice) <= 0) {
            throw new ServiceUnavailable("Сервис временно недоступен: в призовом пуле недостаточно баланса для выдачи итогового скина");
        }

        return skinCardRepository.findClosestByPrice(wonSkinPrice)
                .orElseThrow(() -> new NotFound("Не найденно никаких скинов, с ценой, близкой к " + wonSkinPrice));
    }

}
