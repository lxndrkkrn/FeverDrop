package dto.skins;

import java.math.BigDecimal;

public record SkinPriceUpdateDto(
        Long id,

        String fullName,
        String skinMarketId,
        BigDecimal price
) {
}
