package dto.minigames.casegame;

import java.math.BigDecimal;

public record SendWonSkinToKafkaDto(
        String fullName,
        BigDecimal marketPrice,
        String rarity,
        String imageUrl,
        Long userId
) {
}
