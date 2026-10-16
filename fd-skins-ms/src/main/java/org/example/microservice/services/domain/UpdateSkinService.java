package org.example.microservice.services.domain;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.dtos.MarketInventoryDto;
import org.example.microservice.dtos.MarketItemDto;
import org.example.microservice.entities.Skin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class UpdateSkinService {

    private final SkinService skinService;
    private RestClient restClient;

    @Value("${market.api.base-url:https://aim.market}")
    private String baseUrl;

    @Value("${market.api.key}")
    private String apiKey;

    // Настройка REST клиента (C[Сервер] -> S[Aim.Market])
    @PostConstruct
    public void init() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(15000);

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", "application/json")
                .requestFactory(requestFactory)
                .build();
    }

    // Метод Get запроса на Aim.Market
    public MarketInventoryDto fetchSkinsFromMarket(String cursor) {
        return restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder
                            .path("/inventories/get")
                            .queryParam("apiKey", apiKey)
                            .queryParam("appId", 730)
                            .queryParam("tradable", true)
                            .queryParam("limit", 50)
                            .queryParam("deliveryType", "INSTANT");

                    if (cursor != null && !cursor.isBlank()) {
                        uriBuilder.queryParam("cursor", cursor);
                    }

                    return uriBuilder.build();
                })
                .retrieve()
                .body(MarketInventoryDto.class);
    }

    // Проверка ответа от Aim.Market
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean isResponseEmpty(MarketInventoryDto dto) {
        if (dto == null || dto.items() == null || dto.items().isEmpty()) {
            log.warn("Aim.Market прислал пустой JSON или список скинов");
            return true;
        } else {
            log.info("Aim.Market прислал валидный список скинов");
            return false;
        }
    }

    // Составление мапы действительных скинов
    @Transactional(propagation = Propagation.MANDATORY)
    public Map<String, Skin> getExistingSkinsMap() {
        return skinService.findAllSkins().stream()
                .collect(Collectors.toMap(Skin::getSkinMarketId, skin -> skin));
    }

    // Создание обновленного списка скинов:
    // 1. Перебор списка предметов-дто
    // 2. Пропуск только Трейдабельные
    // 3. Если скин уже есть в базе -> | Обновление даты парсинга
    //                                 | Обновление цены (если была изменена на маркете)
    //    Если скин новый и в базе его нет -> | Создание нового экземпляра скина
    // 4. Отправка списка обновленных скинов
    @Transactional(propagation = Propagation.MANDATORY)
    public List<Skin> processUpdate(List<MarketItemDto> items, Map<String, Skin> existingSkins) {
        List<Skin> modifedSkinList = new ArrayList<>();

        for (MarketItemDto item : items) {
            if (item.tradable()) {

                Skin skin;

                if (existingSkins.containsKey(item.skinMarketId())) {

                    skin = existingSkins.get(item.skinMarketId());

                    skin.setLastSeenAt(LocalDateTime.now());

                    if (skin.getMarketPrice().compareTo(item.marketPrice()) == 0) {
                        skin.updatePriceFromMarket(item.marketPrice());
                    }

                } else {
                    skin = createNewSkinTemplate(item);

                    existingSkins.put(item.skinMarketId(), skin);
                    skin.setLastSeenAt(LocalDateTime.now());

                }
                modifedSkinList.add(skin);
            }
        }

        return modifedSkinList;
    }

    // Создание экземпляра скина
    @Transactional(propagation = Propagation.MANDATORY)
    public Skin createNewSkinTemplate(MarketItemDto externalItem) {
        Skin newSkin = new Skin();
        newSkin.setSkinMarketId(externalItem.skinMarketId());
        newSkin.setName(externalItem.marketHashName());
        newSkin.setMarketPrice(externalItem.marketPrice());
        newSkin.setSkinFloat(externalItem.skinFloat());
        newSkin.setImageUrl(null);

        return newSkin;
    }

}
