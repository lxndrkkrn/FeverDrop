package org.example.microservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record MarketItemDto(
        @JsonProperty("id") String skinMarketId,
        @JsonProperty("marketHashName") String marketHashName,
        @JsonProperty("price") BigDecimal marketPrice,
        @JsonProperty("steamPrice") BigDecimal steamPrice,
        @JsonProperty("type") String type,
        @JsonProperty("rarity") String rarity,
        @JsonProperty("exterior") String wear,
        @JsonProperty("quality") String mod,
        @JsonProperty("float") Float skinFloat,
        @JsonProperty("tradable") boolean tradable
) { }
