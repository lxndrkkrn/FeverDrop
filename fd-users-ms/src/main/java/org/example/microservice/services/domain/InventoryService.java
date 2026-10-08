package org.example.microservice.services.domain;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.domain.client.NotFound;
import org.example.microservice.entities.Inventory;
import org.example.microservice.entities.SkinId;
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
    public Collection<SkinId> addSkins(Collection<SkinId> list, Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new NotFound("Инвентарь не найден"));

        inventory.getSkins().addAll(list); // Теперь Hibernate сделает только точечные INSERT
        return list;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Collection<SkinId> takeSkins(Collection<SkinId> list, Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new NotFound("Инвентарь не найден"));

        inventory.getSkins().removeAll(list); // Теперь Hibernate сделает только точечные DELETE по конкретным ID
        return list;
    }

}
