package dto.minigames.contractgame;

import dto.minigames.casegame.SendWonSkinToKafkaDto;

public record SendResultContractToKafkaDto(
        Long sessionId,
        SendWonSkinToKafkaDto skinDto,
        Long userId

) {
}
