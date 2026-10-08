package org.example.microservice.services.domain;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.domain.client.NotFound;
import org.example.microservice.entities.SkinId;
import org.example.microservice.repositories.SkinIdRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class SkinIdService {

    private final SkinIdRepository skinIdRepository;

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public SkinId findSkinIdById(Long id) {
        return skinIdRepository.findById(id)
                .orElseThrow(() -> new NotFound("SkinId не найден"));
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public SkinId findSkinIdBySkinId(Long skinId) {
        return skinIdRepository.findSkinIdBySkinId(skinId)
                .orElseThrow(() -> new NotFound("SkinId не найден"));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public SkinId createSkinId(Long skinId) {

        SkinId skin = new SkinId();

        skin.setSkinId(skinId);

        skinIdRepository.save(skin);

        return skin;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteSkinId(Long id) {
        skinIdRepository.deleteById(id);
    }

}
