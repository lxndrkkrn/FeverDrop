package dto.minigames.casegame;

public record SendWonSkinToKafkaDto(
        String fullName,
        Long userId
) {
}
