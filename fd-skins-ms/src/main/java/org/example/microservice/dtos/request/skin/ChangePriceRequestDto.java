package org.example.microservice.dtos.request.skin;

import java.math.BigDecimal;

public record ChangePriceRequestDto(
        Long id,
        BigDecimal price
) {
}
