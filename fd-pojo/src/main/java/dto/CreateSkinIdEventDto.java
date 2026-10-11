package dto;

public record CreateSkinIdEventDto(
        String name,
        String skinMarketId,
        Long id
) {
}
