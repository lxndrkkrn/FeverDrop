package dto;

import java.util.List;

public record SkinsPricesUpdateEventDto(
        List<SkinPriceUpdateDto> updatedSkins,
        Long timestamp
) {
}
