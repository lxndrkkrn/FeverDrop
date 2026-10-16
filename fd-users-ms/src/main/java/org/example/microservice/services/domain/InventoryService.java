package org.example.microservice.services.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j

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

//    @Transactional(propagation = Propagation.MANDATORY)
//    public void addSkinsToInventory(Inventory inventory, List<String> marketIds) {
//        log.info("Добавление пачки скинов в инвентарь {}", inventory.getId());
//
//        for (String marketId : marketIds) {
//            skinInstService.createSkinInst(marketId, inventory);
//        }
//    }
//
//    @Transactional(propagation = Propagation.MANDATORY)
//    public void removeSkinsFromInventory(Inventory inventory, List<Long> skinInstIds) {
//        log.info("Удаление пачки скинов из инвентаря {}", inventory.getId());
//
//        for (Long id : skinInstIds) {
//            skinInstService.deleteSkinInst(id);
//        }
//    }

}
