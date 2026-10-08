package org.example.microservice.dtos;

import java.math.BigDecimal;

public record AimMarketItemDto(
        String skinMarketId,
        String marketHashName,
        BigDecimal price,
        String type,
        String rarity,
        String exterior,
        Float Float
) {
}
