package org.example.module.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record WalletResponseDto(
        @JsonProperty("id") String accountId,
        @JsonProperty("balance") BigDecimal balance,
        @JsonProperty("currency") String currency
) {
}
