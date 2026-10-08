package org.example.microservice.dtos.request;

import java.math.BigDecimal;

public record ChangePriceRequestDto(
        Long id,
        BigDecimal price
) {
}
