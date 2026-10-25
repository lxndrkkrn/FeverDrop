package org.example.microservice.services.upgradegame;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.domain.client.BadRequest;
import org.example.microservice.components.systemwallet.SystemWalletCache;
import org.example.microservice.dtos.UpgradeResponseDto;
import org.example.microservice.entities.SkinCard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpgradeGameHandle {

    private final SystemWalletCache systemWalletCache;
    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public UpgradeResponseDto getWonSkinOrNull(BigDecimal allPrice, SkinCard marketSkin) {
        BigDecimal totalBalance = systemWalletCache.getTotalBalance();
        BigDecimal prizePool = systemWalletCache.getPrizePool();

        if (totalBalance == null || prizePool == null) {
            throw new org.example.exceptions.domain.server.ServiceUnavailable("Сервис временно недоступен: Баланс проекта не синхронизирован");
        }

        BigDecimal marketSkinPrice = marketSkin.getMarketPrice();

        if (allPrice.signum() <= 0 || marketSkinPrice.signum() <= 0) {
            throw new BadRequest("Цена скинов должна быть строго больше нуля");
        }

        BigDecimal chance = allPrice.divide(marketSkinPrice, 4, RoundingMode.DOWN);

        if (chance.compareTo(new BigDecimal("0.75")) > 0 || chance.compareTo(new BigDecimal("0.0001")) < 0) {
            throw new BadRequest("Шанс на апгрейд должен быть строго от 0.01% до 75%");
        }

        if (marketSkinPrice.compareTo(prizePool) > 0) {
            log.info("Целевой скин дороже призового пула. Принудительный авто-проигрыш.");

            double fakeHighShot = secureRandom.nextDouble(0.7501, 1.0000);
            return new UpgradeResponseDto("LOSE", BigDecimal.valueOf(fakeHighShot).setScale(4, RoundingMode.DOWN));
        }

        double randomValue = secureRandom.nextDouble();
        BigDecimal randomShot = BigDecimal.valueOf(randomValue).setScale(4, RoundingMode.DOWN);

        log.info("Расчет апгрейда. Шанс игрока: {}. Выстрел рулетки: {}", chance, randomShot);

        if (chance.compareTo(randomShot) >= 0) {
            return new UpgradeResponseDto("WIN", randomShot);
        } else {
            return new UpgradeResponseDto("LOSE", randomShot);
        }
    }
}
