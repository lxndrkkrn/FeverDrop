package org.example.microservice.services.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.dtos.request.skin.ChangePriceRequestDto;
import org.example.microservice.dtos.request.skin.CreateSkinRequestDto;
import org.example.microservice.dtos.request.skin.DeleteSkinRequestDto;
import org.example.microservice.dtos.response.skin.CreateSkinResponseDto;
import org.example.microservice.entities.Skin;
import org.example.microservice.services.domain.SkinService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j

public class SkinAppService {

    private final SkinService skinService;

    @Transactional
    public CreateSkinResponseDto createSkin(CreateSkinRequestDto dto) {
        log.info("Попытка создания скина: {} {} | {} ({} - {}), {}; MP: {}; SP: {}; skinMarketId: {}", dto.mod(),  dto.type(), dto.name(), dto.wear(), dto.skinFloat(), dto.rarity(), dto.marketPrice(), dto.steamPrice(), dto.skinMarketId());

        Skin skin = skinService.createSkin(
                dto.skinMarketId(),
                dto.name(),
                dto.skinFloat(),
                dto.marketPrice(),
                dto.steamPrice(),
                dto.imageUrl(),
                dto.type(),
                dto.rarity(),
                dto.wear(),
                dto.mod()
        );

        log.info("Создан скин {} {} | {} ({} - {}), {}; MP: {}; SP: {}; skinMarketId: {}. Его ID: {}", dto.mod(),  dto.type(), dto.name(), dto.wear(), dto.skinFloat(), dto.rarity(), dto.marketPrice(), dto.steamPrice(), dto.skinMarketId(), skin.getId());

        return new CreateSkinResponseDto(
                skin.getSkinMarketId(),
                skin.getName(),
                skin.getSkinFloat(),
                skin.getMarketPrice(),
                skin.getSteamPrice(),
                skin.getImageUrl(),
                skin.getType(),
                skin.getRarity(),
                skin.getWear(),
                skin.getMod()
        );
    }

    @Transactional
    public void deleteSkin(DeleteSkinRequestDto dto) {
        log.info("Попытка удаления скина с ID: {}", dto.id());

        skinService.deleteSkin(dto.id());

        log.info("Скин удалён");
    }

    @Transactional
    public void changeMarketPrice(ChangePriceRequestDto dto) {
        log.info("Попытка сменить цену (по маркету) скину с ID: {} на {}", dto.id(), dto.price());

        skinService.changeMarketPrice(dto.id(), dto.price());

        log.info("Цена (по маркету) на скин с ID: {} успешно изменена", dto.id());
    }

    @Transactional
    public void changeSteamPrice(ChangePriceRequestDto dto) {
        log.info("Попытка сменить цену (по стиму) скину с ID: {} на {}", dto.id(), dto.price());

        skinService.changeSteamPrice(dto.id(), dto.price());

        log.info("Цена (по стиму) на скин с ID: {} успешно изменена", dto.id());
    }

}
