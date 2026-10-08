package org.example.microservice.dtos.request.balance;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AddBalanceRequestDto(
        @NotNull Long id,
        @NotNull @Positive BigDecimal delta
) {
}
