package org.example.microservice.components.systemwallet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
@RequiredArgsConstructor

public class SystemWalletCache {

    private final AtomicReference<BigDecimal> totalBalance = new AtomicReference<>(BigDecimal.ZERO);
    private final AtomicReference<BigDecimal> prizePool = new AtomicReference<>(BigDecimal.ZERO);

    public void update(BigDecimal totalBalance, BigDecimal prizePool) {
        log.info("Обновление кэша баланса сайта: TotalBalance: {}, PrizePool: {}", totalBalance, prizePool);

        this.totalBalance.set(totalBalance);
        this.prizePool.set(prizePool);
    }

    public BigDecimal getTotalBalance() {
        return this.totalBalance.get();
    }

    public BigDecimal getPrizePool() {
        return this.prizePool.get();
    }

}
