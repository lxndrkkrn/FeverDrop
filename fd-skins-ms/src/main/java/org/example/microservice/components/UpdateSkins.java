package org.example.microservice.components;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.example.microservice.dtos.MarketInventoryDto;
import org.example.microservice.dtos.MarketItemDto;
import org.example.microservice.dtos.request.skin.CreateSkinRequestDto;
import org.example.microservice.entities.Skin;
import org.example.microservice.services.application.SkinAppService;
import org.example.microservice.services.domain.SkinService;
import org.example.microservice.services.domain.UpdateSkinService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j

public class UpdateSkins {

    private final SkinService skinService;
    private final UpdateSkinService updateSkinService;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void parseAndUpdateSkins() {
        log.info("Начинаем парсинг скинов с API Aim.Market...");

        try {
            String cursor = null;
            boolean hasMorePages = true;

            int totalProcessed = 0;
            int pages = 0;

            Map<String, Skin> existingSkins = updateSkinService.getExistingSkinsMap();

            while (hasMorePages) {

                MarketInventoryDto marketInventory = updateSkinService.fetchSkinsFromMarket(cursor);
                if (updateSkinService.isResponseEmpty(marketInventory)) break;

                updateSkinService.processUpdate(marketInventory.items(), existingSkins);

                totalProcessed += marketInventory.items().size();
                pages += 1;

                cursor = marketInventory.cursor();
                if (cursor == null || cursor.isBlank()) {
                    hasMorePages = false;
                } else {
                    log.info("Страница №{} обработана. TotalProcessed: {}; Coursor: {}", pages, totalProcessed, cursor);
                }

            }

            if (totalProcessed > 0) {
                skinService.saveAllSkins(List.copyOf(existingSkins.values()));
                log.info("Синхронизация полностью завершена! Всего обработано скинов со всех страниц: {}. В базе: {}",
                        totalProcessed, existingSkins.size());
            }

        } catch (RestClientResponseException e) {

            log.error("Aim.Market вернул ошибку HTTP. Статус-код: {}, Тело ответа: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());

        } catch (Exception e) {
            log.error("Критическая ошибка при парсинге скинов", e);
        }
    }

}
