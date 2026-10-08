package org.example.microservice.dtos.request.user;

import jakarta.validation.constraints.NotNull;

public record DeleteUserRequestDto(
        @NotNull Long id
) {
}
