package dto.minigames;

import java.math.BigDecimal;

public record UserOpenedCaseResponse(
        Long userId,
        BigDecimal casePrice,
        String skinFullName
) {
}
