package org.example.microservice.components;

import dto.minigames.casegame.SendWonSkinToKafkaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.entities.Inventory;
import org.example.microservice.entities.User;
import org.example.microservice.services.domain.InventoryService;
import org.example.microservice.services.domain.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor

public class CaseGameListener {

    private final UserService userService;
    private final InventoryService inventoryService;

    @KafkaListener(topics = "won-skin-response-topic", groupId = "users-microservice")
    @Transactional
    public void getWonSkinHandle(SendWonSkinToKafkaDto event) {
        try {
            User user = userService.findUserById(event.userId());
            Inventory inventory = user.getInventory();

            inventoryService.addSkinToInventory(inventory, event.fullName(), event.rarity(), event.imageUrl(), event.marketPrice());

        } catch (Exception e) {
            log.error("Ошибка при выдаче скина (сторона слушателя)", e);
            throw e;
        }
    }

    // SALE METHOD

}
