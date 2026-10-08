package org.example.microservice.dtos.response.user;

public record CreateUserResponseDto(
        Long id,
        String email,
        String tradeUrl,
        Long inventoryId
) {
}
