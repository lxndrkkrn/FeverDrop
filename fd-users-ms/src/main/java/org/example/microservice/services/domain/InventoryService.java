package org.example.microservice.services.domain;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.domain.client.NotFound;
import org.example.microservice.entities.Inventory;
import org.example.microservice.entities.User;
import org.example.microservice.repositories.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor

public class InventoryService {

    private final InventoryRepository inventoryRepository;

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
    public void addSkinsByMarketIds(Collection<String> marketIds, Long inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFound("Инвентарь не найден"));

        inventory.getSkinMarketIds().addAll(marketIds);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void takeSkinsByMarketIds(Collection<String> marketIds, Long inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFound("Инвентарь не найден"));

        inventory.getSkinMarketIds().removeAll(marketIds);
    }

}
