package dto.skins;

import java.util.List;

public record SkinsPricesUpdateEventDto(
        List<SkinPriceUpdateDto> updatedSkins,
        Long timestamp
) {
}
