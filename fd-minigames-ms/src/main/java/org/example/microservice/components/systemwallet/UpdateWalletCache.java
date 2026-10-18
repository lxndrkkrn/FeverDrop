package org.example.microservice.components.systemwallet;

import dto.systemwallet.WalletBalanceUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor

public class UpdateWalletCache {

    private final SystemWalletCache systemWalletCache;

    @KafkaListener(topics = "wallet-update-topic", groupId = "minigames-group")
    public void SystemWalletUpdateHandler(WalletBalanceUpdateDto event) {
        log.info("Пришёл обновленный SystemWallet, кэшируем...");

        try {
            if (event == null || event.prizePool() == null || event.totalBalance() == null) {
                log.warn("Обновление пришло с пустым ДТО / Общим балансом / Призовым пулом");
                return;
            }

            systemWalletCache.update(event.totalBalance(), event.prizePool());
            log.info("Кэш SystemWallet обновлен");

        } catch (Exception e) {
            log.error("Ошибка при кэшировании обновления SystemWallet");
        }
    }

}
