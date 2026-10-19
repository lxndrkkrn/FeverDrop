package dto.minigames.casegame;

import java.math.BigDecimal;

public record UserOpenedCaseResponse(
        Long userId,
        BigDecimal casePrice,
        String skinFullName
) {
}
