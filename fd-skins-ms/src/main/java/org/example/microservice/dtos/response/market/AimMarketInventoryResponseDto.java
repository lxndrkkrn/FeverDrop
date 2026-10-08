package org.example.microservice.dtos.response.market;

import org.example.microservice.dtos.AimMarketItemDto;

import java.util.List;

public record AimMarketInventoryResponseDto(
        List<AimMarketItemDto> items,
        String cursor
) {
}
