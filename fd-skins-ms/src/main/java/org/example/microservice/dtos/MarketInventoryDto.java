package org.example.microservice.dtos;

import java.util.List;
import java.util.Set;

public record MarketInventoryDto(
        Set<MarketItemDto> items,
        String cursor
) {
}
