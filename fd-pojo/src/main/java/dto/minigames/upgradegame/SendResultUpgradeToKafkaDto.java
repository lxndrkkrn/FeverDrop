package dto.minigames.upgradegame;

import dto.minigames.casegame.SendWonSkinToKafkaDto;

public record SendResultUpgradeToKafkaDto(
        Long userId,
        String result,
        SendWonSkinToKafkaDto skin,
        Long sessionId
) {
}
