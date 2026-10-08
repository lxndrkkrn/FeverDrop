package org.example.microservice.dtos.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ChangeUserEmailRequestDto(
        @NotNull Long id,
        @NotNull @Email String email
) {
}
