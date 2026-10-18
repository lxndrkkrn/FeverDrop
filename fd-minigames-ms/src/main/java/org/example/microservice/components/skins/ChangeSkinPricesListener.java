package org.example.microservice.components.skins;

import dto.skins.SkinPriceUpdateDto;
import dto.skins.SkinsPricesUpdateEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.entities.SkinCard;
import org.example.microservice.services.skincard.SkinCardService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor

public class ChangeSkinPricesListener {

    private final SkinCardService skinCardService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "update-marketPrice-card-topic", groupId = "minigames-group")
    public void updateMarketPriceCardHandler(SkinsPricesUpdateEventDto event) {
        log.info("Пришла новая пачка скинов из парсера. Обработка...");

        try {
            List<SkinPriceUpdateDto> updateList = event.updatedSkins();

            if (updateList == null || updateList.isEmpty()) return;

            updateMarketPrice(updateList);

        } catch (Exception e) {
            log.error("Ошибка при обработке пакета цен из Kafka", e);
        }
    }

    @Transactional
    public void updateMarketPrice(List<SkinPriceUpdateDto> updateList) {

        List<String> nameToUpdate = updateList.stream()
                .map(SkinPriceUpdateDto::fullName)
                .toList();

        Map<String, SkinCard> existingSkinCards = skinCardService.findAllByFullName(nameToUpdate).stream()
                .collect(Collectors.toMap(SkinCard::getFullName, card -> card, (oldValue, newValue) -> oldValue));

        if (existingSkinCards.isEmpty()) {
            log.info("В микросервисе мини-игр пока нет карточек для прилетевших скинов. Пропускаем.");
            return;
        }

        List<SkinCard> skinsForUpdate = new ArrayList<>();

        for (SkinPriceUpdateDto item : updateList) {
            String fullName = item.fullName();

            if (existingSkinCards.containsKey(fullName)) {
                SkinCard card = existingSkinCards.get(fullName);

                if (card.getMarketPrice().compareTo(item.price()) != 0) {

                    card.setMarketPrice(item.price());
                    skinsForUpdate.add(card);
                }
            }
        }

        if (!skinsForUpdate.isEmpty()) {

            skinCardService.saveAll(skinsForUpdate);
            log.info("Цены успешно обновлены для {} карточек SkinCard.", skinsForUpdate.size());

        } else {
            log.info("Цены в событии совпали с текущими. Обновление БД не требуется.");
        }

    }

}
