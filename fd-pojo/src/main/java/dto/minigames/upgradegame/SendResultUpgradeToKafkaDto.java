package dto.minigames.upgradegame;

public record SendResultUpgradeToKafkaDto(
        Long userId,
        String result,
        Long sessionId
) {
}
