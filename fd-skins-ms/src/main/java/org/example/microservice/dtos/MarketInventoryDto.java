package org.example.microservice.dtos;

import java.util.List;

public record MarketInventoryDto(
        List<MarketItemDto> items,
        String cursor
) {
}
