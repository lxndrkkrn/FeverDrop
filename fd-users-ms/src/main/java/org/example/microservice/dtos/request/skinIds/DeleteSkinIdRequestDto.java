package org.example.microservice.dtos.request.skinIds;

import jakarta.validation.constraints.NotNull;

public record DeleteSkinIdRequestDto(
        @NotNull Long id
) {
}
