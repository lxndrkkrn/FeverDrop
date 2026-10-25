package org.example.microservice.services.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.domain.client.NotFound;
import org.example.microservice.entities.Inventory;
import org.example.microservice.entities.SkinInst;
import org.example.microservice.entities.User;
import org.example.microservice.repositories.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.example.exceptions.domain.client.Forbidden;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final SkinInstService skinInstService;

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public Inventory findInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new NotFound("Инвентарь не найден"));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Inventory createInventory(User user) {
        Inventory inventory = new Inventory();

        inventory.setUser(user);

        inventoryRepository.save(inventory);

        return inventory;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void addSkinToInventory(Inventory inventory, String fullName, String rarity, String imageUrl, BigDecimal marketPrice) {

        SkinInst skinInst = new SkinInst();

        skinInst.setFullName(fullName);
        skinInst.setRarity(rarity);
        skinInst.setImageUrl(imageUrl);
        skinInst.setMarketPrice(marketPrice);

        inventory.addSkinInstToInventory(skinInst);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void takeSkinFromInventory(Inventory inventory, Long id) {
        SkinInst skinInst = skinInstService.findById(id);

        if (!skinInst.getInventory().getUser().getId().equals(inventory.getUser().getId())) {
            throw new Forbidden("Вы не можете использовать чужой скин!");
        }

        if (skinInst.isFrozen()) {
            throw new Forbidden("Скин заморожен в другой игровой сессии (например, в Апгрейде)!");
        }

        inventory.takeSkinInstFromInventory(skinInst);
    }
}
