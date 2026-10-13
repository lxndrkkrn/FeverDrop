package dto;

import java.math.BigDecimal;

public record SkinPriceUpdateDto(
        Long id,
        String skinMarketId,
        BigDecimal price
) {
}
