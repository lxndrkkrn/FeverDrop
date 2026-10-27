package org.example.microservice.dtos;

import dto.minigames.casegame.SendWonSkinToKafkaDto;

public record ContractResponseDto(
        SendWonSkinToKafkaDto skinDto
) {
}
