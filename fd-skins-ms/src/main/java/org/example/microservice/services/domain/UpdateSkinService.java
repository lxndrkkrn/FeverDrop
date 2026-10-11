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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Transactional(propagation = Propagation.MANDATORY)
    public MarketInventoryDto fetchSkinsFromMarket(String cursor) {
        return restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder
                            .path("/inventories/get")
                            .queryParam("apiKey", apiKey)
                            .queryParam("appId", 730);

                    if (cursor != null && !cursor.isBlank()) {
                        uriBuilder.queryParam("cursor", cursor);
                    }

                    return uriBuilder.build();
                })
                .retrieve()
                .body(MarketInventoryDto.class);
    }

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

    @Transactional(propagation = Propagation.MANDATORY)
    public Map<String, Skin> getExistingSkinsMap() {
        return skinService.findAllSkins().stream()
                .collect(Collectors.toMap(Skin::getSkinMarketId, skin -> skin));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public List<Skin> processUpdate(List<MarketItemDto> items, Map<String, Skin> existingSkins) {
        List<Skin> newSkinList = new ArrayList<>();

        for (MarketItemDto item : items) {
            if (item.tradable()) {
                if (existingSkins.containsKey(item.skinMarketId())) {

                    Skin skin = existingSkins.get(item.skinMarketId());
                    skin.updatePriceFromMarket(item.marketPrice());

                } else {
                    Skin skin = createNewSkinTemplate(item);
                    existingSkins.put(item.skinMarketId(), skin);

                    newSkinList.add(skin);
                }
            }
        }

        return newSkinList;
    }

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
