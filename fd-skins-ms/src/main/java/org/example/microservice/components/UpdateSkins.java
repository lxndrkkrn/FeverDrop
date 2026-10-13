package org.example.microservice.components;

import dto.SkinPriceUpdateDto;
import dto.SkinsPricesUpdateEventDto;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.rmi.ServerError;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j

public class UpdateSkins {

    private final SkinService skinService;
    private final UpdateSkinService updateSkinService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final int BATCH_SIZE = 1000;

    @Scheduled(fixedDelay = 3600000)
    //@Transactional
    public void parseAndUpdateSkins() {
        log.info("Начинаем парсинг скинов с API Aim.Market...");

        try {
            // Дата начала парса
            LocalDateTime parseDate = LocalDateTime.now();

            String cursor = null;
            boolean hasMorePages = true;

            int totalProcessed = 0;
            int pages = 0;

            Map<String, Skin> existingSkins = updateSkinService.getExistingSkinsMap();

            List<Skin> changesBuff = new ArrayList<>();
            List<SkinPriceUpdateDto> kafkaSkinsList = new ArrayList<>();

            // Цикл парса маркета (Пока есть что парсить)
            while (hasMorePages) {

                // Get запрос на маркет + проверка
                MarketInventoryDto marketInventory = updateSkinService.fetchSkinsFromMarket(cursor);
                if (updateSkinService.isResponseEmpty(marketInventory)) {
                    log.info("Получен пустой ответ от API. Завершаем чтение страниц.");
                    break;
                }

                // Страница со скинами + обновление статистики
                cursor = marketInventory.cursor();

                totalProcessed += marketInventory.items().size();
                pages ++;

                // Добавление обновленных скинов в список Изменённых скинов
                List<Skin> updateSkins = updateSkinService.processUpdate(marketInventory.items(), existingSkins);
                changesBuff.addAll(updateSkins);

                for (Skin skin : updateSkins) {
                    kafkaSkinsList.add(new SkinPriceUpdateDto(skin.getId(), skin.getSkinMarketId(), skin.getMarketPrice()));
                }

                log.info("Страница {} обработана. Скинов в буфере: {}", pages, changesBuff.size());

                // Если куча >= BATCH_SIZE, сохраняем
                if (changesBuff.size() >= BATCH_SIZE) {

                    skinService.saveAllSkins(new ArrayList<>(changesBuff));
                    changesBuff.clear();

                    if (!kafkaSkinsList.isEmpty()) {
                        SkinsPricesUpdateEventDto event = new SkinsPricesUpdateEventDto(
                                kafkaSkinsList,
                                System.currentTimeMillis()
                        );

                        kafkaTemplate.send("skins-change-prices-topic", "batch", event);
                        kafkaSkinsList.clear();

                        log.info("Пакет обновлений цен отправлен в Kafka.");
                    }

                    kafkaSkinsList.clear();

                    log.info("Синхронизация полностью завершена! Всего обработано скинов со всех страниц: {}. В базе: {}",
                            totalProcessed, existingSkins.size());
                }

                // Если странцы нет, останавливаем парс маркета
                if (cursor == null || cursor.isBlank()) {
                    hasMorePages = false;
                } else {
                    log.info("Страница №{} обработана. TotalProcessed: {}; Coursor: {}", pages, totalProcessed, cursor);
                }
            }

            // Досохраняем остатки
            if (!changesBuff.isEmpty()) {
                skinService.saveAllSkins(new ArrayList<>(changesBuff));
                changesBuff.clear();
            }

            if (!kafkaSkinsList.isEmpty()) {
                SkinsPricesUpdateEventDto event = new SkinsPricesUpdateEventDto(
                        kafkaSkinsList,
                        System.currentTimeMillis()
                );

                kafkaTemplate.send("skins-change-prices-topic", "batch", event);
                kafkaSkinsList.clear();
            }

            // Отчёт о парсе
            int deleteSkins = skinService.deleteObsoleteSkins(parseDate);

            log.info("Удалено скинов: {}", deleteSkins);

            log.info("Синхронизация полностью завершена! Всего обработано скинов со всех страниц: {}. В базе: {}",
                    totalProcessed, existingSkins.size());

        } catch (RestClientResponseException e) {

            log.error("Aim.Market вернул ошибку HTTP. Статус-код: {}, Тело ответа: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());

        } catch (Exception e) {
            log.error("Критическая ошибка при парсинге скинов", e);
        }
    }

}
