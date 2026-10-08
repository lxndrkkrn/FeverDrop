package org.example.microservice.services.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.dtos.request.Inventory.AddSkinsRequestDto;
import org.example.microservice.dtos.request.Inventory.TakeSkinsRequestDto;
import org.example.microservice.entities.SkinId;
import org.example.microservice.services.domain.InventoryService;
import org.example.microservice.services.domain.SkinIdService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j

public class InventoryAppService {

    private final InventoryService inventoryService;
    private final SkinIdService skinIdService;

    @Transactional
    public void addSkinsByRealIds(@Validated AddSkinsRequestDto dto) {
        log.info("Попытка добавления скинов по их Real Id ({}) игроку {}", dto.ids(), dto.id());

        Set<SkinId> skinIdsSet = dto.ids().stream()
                .map(skinIdService::findSkinIdBySkinId)
                .collect(Collectors.toSet());

        inventoryService.addSkins(skinIdsSet, dto.id());
        log.info("Скины ({}) добавлены игроку {}", dto.ids(), dto.id());
    }

    @Transactional
    public void takeSkinsByRealIds(@Validated TakeSkinsRequestDto dto) {
        log.info("Попытка удаления скинов по их Real Id ({}) игроку {}", dto.ids(), dto.id());

        Set<SkinId> skinIdsSet = dto.ids().stream()
                .map(skinIdService::findSkinIdBySkinId)
                .collect(Collectors.toSet());

        inventoryService.takeSkins(skinIdsSet, dto.id());
        log.info("Скины ({}) удалены у игрока {}", dto.ids(), dto.id());
    }

}
