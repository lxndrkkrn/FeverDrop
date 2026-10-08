package org.example.microservice.dtos.request.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangeUserPasswordRequestDto(
        @NotNull Long id,
        @Size(min = 6) @NotNull String password
) {
}
