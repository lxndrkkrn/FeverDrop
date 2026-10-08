package org.example.microservice.services.domain;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.domain.client.NotFound;
import org.example.microservice.entities.Skin;
import org.example.microservice.enums.WeMod;
import org.example.microservice.enums.WeRarity;
import org.example.microservice.enums.WeType;
import org.example.microservice.enums.WeWear;
import org.example.microservice.repositories.SkinRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor

public class SkinService {

    private final SkinRepository skinRepository;

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public Skin findSkinById(Long id) {
        return skinRepository.findById(id)
                .orElseThrow(() -> new NotFound("Скин не найден"));
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public Skin findSkinBySkinMarketId(String id) {
        return skinRepository.findSkinBySkinMarketId(id)
                .orElseThrow(() -> new NotFound("Скин не найден"));
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public List<Skin> findSkinByPriceBetween(BigDecimal min, BigDecimal max) {
        return skinRepository.findSkinsByMarketPriceBetween(min, max);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Skin createSkin(
            String skinMarketId,
            String name,
            Float skinFloat,
            BigDecimal marketPrice,
            BigDecimal steamPrice,
            String imageUrl,
            WeType type,
            WeRarity rarity,
            WeWear wear,
            WeMod mod
    ) {

        Skin skin = new Skin();

        skin.setSkinMarketId(skinMarketId);
        skin.setName(name);
        skin.setSkinFloat(skinFloat);
        skin.setMarketPrice(marketPrice);
        skin.setSteamPrice(steamPrice);
        skin.setImageUrl(imageUrl);
        skin.setType(type);
        skin.setRarity(rarity);
        skin.setWear(wear);
        skin.setMod(mod);

        skinRepository.save(skin);

        return skin;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteSkin(Long id) {
        skinRepository.deleteById(id);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void changeMarketPrice(Long id, BigDecimal price) {
        Skin skin = skinRepository.findById(id)
                .orElseThrow(() -> new NotFound("Скин не найден"));

        skin.setMarketPrice(price);

        skinRepository.save(skin);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void changeSteamPrice(Long id, BigDecimal price) {
        Skin skin = skinRepository.findById(id)
                .orElseThrow(() -> new NotFound("Скин не найден"));

        skin.setSteamPrice(price);

        skinRepository.save(skin);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveAllSkins(List<Skin> list) {
        skinRepository.saveAll(list);
    }

}
