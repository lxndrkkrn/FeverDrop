package org.example.microservice.dtos;

import org.example.microservice.entities.SkinCard;

import java.math.BigDecimal;

public record UpgradeResponseDto(
        String result,
        BigDecimal shotValue
) {
}
