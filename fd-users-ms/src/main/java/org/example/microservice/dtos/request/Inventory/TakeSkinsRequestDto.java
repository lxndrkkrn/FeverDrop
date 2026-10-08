package org.example.microservice.dtos.request.Inventory;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TakeSkinsRequestDto(
        @NotNull List<Long> ids,
        @NotNull Long id
) {
}
