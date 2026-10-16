package org.example.microservice.services.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.domain.client.BadRequest;
import org.example.exceptions.domain.client.NotFound;
import org.example.microservice.entities.Inventory;
import org.example.microservice.entities.SkinInst;
import org.example.microservice.repositories.SkinInstRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor

public class SkinInstService {

    private final SkinInstRepository skinInstRepository;

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public SkinInst findById(Long id) {
        return skinInstRepository.findById(id)
                .orElseThrow(() -> new NotFound("Скин по id не найден"));
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public List<SkinInst> findAllByInventoryAndMarketId(Inventory inventory, String skinMarketId) {
        return skinInstRepository.findAllByInventoryIdAndSkinMarketId(inventory.getId(), skinMarketId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public SkinInst createSkinInst(String fullName, Inventory inventory, BigDecimal marketPrice, String imageUrl, String rarity) {
        log.info("Создание и Привязка нового SkinInst ({}) к инвентарю {}", fullName, inventory.getId());

        if (fullName == null || fullName.isEmpty()) {
            throw new BadRequest("Параметр fullName должен быть не пустой");
        }

        SkinInst skin = new SkinInst();

        skin.setFullName(fullName);
        skin.setMarketPrice(marketPrice);
        skin.setImageUrl(imageUrl);
        skin.setRarity(rarity);

        inventory.addSkinInstToInventory(skin);

        SkinInst savedSkin = skinInstRepository.save(skin);

        log.info("SkinInst успешно создан и добавлен в инвентарь. ID экземпляра: {}", savedSkin.getId());
        return savedSkin;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteSkinInst(Long id) {
        log.info("Удаление и Отвязка SkinInst по ID: {}", id);

        if (id == null) {
            throw new BadRequest("Параметр Id обязателен для удаления конкретного экземпляра");
        }

        SkinInst skin = skinInstRepository.findById(id)
                .orElseThrow(() -> new NotFound("Скин по id не найден"));

        if (skin.getInventory() != null) {
            skin.getInventory().takeSkinInstFromInventory(skin);
        }

        skinInstRepository.delete(skin);
        log.info("SkinInst с ID {} успешно удалён из инвентаря", id);
    }

}
