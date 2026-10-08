package org.example.microservice.services.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.dtos.request.skinIds.CreateSkinIdRequestDto;
import org.example.microservice.dtos.request.skinIds.DeleteSkinIdRequestDto;
import org.example.microservice.dtos.response.skinIds.CreateSkinIdResponseDto;
import org.example.microservice.entities.SkinId;
import org.example.microservice.services.domain.SkinIdService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Slf4j

public class SkinIdAppService {

    private final SkinIdService skinIdService;

    @Transactional
    public CreateSkinIdResponseDto createSkinId(@Validated CreateSkinIdRequestDto dto) {
        log.info("Попытка создания SkinId...");

        SkinId skinId = skinIdService.createSkinId(dto.skinId());

        log.info("SkinId создан, ID: {}, SkinId: {}", skinId.getId(), dto.skinId());

        return new CreateSkinIdResponseDto(
                skinId.getId(),
                dto.skinId()
        );
    }

    @Transactional
    public void deleteSkinId(@Validated DeleteSkinIdRequestDto dto) {
        log.info("Попытка удаления SkinId...");

        skinIdService.deleteSkinId(dto.id());

        log.info("SkinId удалён");
    }

}
