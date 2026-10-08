package org.example.microservice.dtos.request.skin;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.microservice.enums.WeMod;
import org.example.microservice.enums.WeRarity;
import org.example.microservice.enums.WeType;
import org.example.microservice.enums.WeWear;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public record CreateSkinRequestDto(
        @NotNull String skinMarketId,
        @NotNull String name,
        @NotNull @Max(value = 1) @Min(value = 0) Float skinFloat,
        @NotNull @Positive BigDecimal marketPrice,
        @Positive BigDecimal steamPrice,
        @NotNull @URL String imageUrl,
        @NotNull WeType type,
        @NotNull WeRarity rarity,
        @NotNull WeWear wear,
        @NotNull WeMod mod
) {
}
