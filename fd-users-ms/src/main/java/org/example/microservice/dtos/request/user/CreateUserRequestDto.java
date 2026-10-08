package org.example.microservice.dtos.request.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record CreateUserRequestDto(
        @NotNull
        @Email
        @Column(unique = true)
        String email,

        @NotNull
        @Size(min = 6)
        String password,

        @NotNull
        @URL
        String tradeUrl
) {
}
