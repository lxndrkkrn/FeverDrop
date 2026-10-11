package org.example.microservice.dtos.request.skin;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public record CreateSkinRequestDto(
        String skinMarketId,
        String name,
        Float skinFloat,
        BigDecimal marketPrice,
        BigDecimal steamPrice,
        String imageUrl,
        String type,
        String rarity,
        String wear,
        String mod
) {
}
