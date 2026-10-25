package org.example.microservice.components;

import dto.minigames.casegame.SendWonSkinToKafkaDto;
import dto.minigames.upgradegame.SendResultUpgradeToKafkaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.domain.client.BadRequest;
import org.example.exceptions.domain.client.NotFound;
import org.example.microservice.entities.Inventory;
import org.example.microservice.entities.SkinInst;
import org.example.microservice.entities.User;
import org.example.microservice.entities.outbox.UpgradeSession;
import org.example.microservice.repositories.outbox.UpgradeSessionRepository;
import org.example.microservice.services.domain.InventoryService;
import org.example.microservice.services.domain.SkinInstService;
import org.example.microservice.services.domain.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor

public class UpgradeGameListener {

    private final UserService userService;
    private final InventoryService inventoryService;
    private final SkinInstService skinInstService;

    private final UpgradeSessionRepository upgradeSessionRepository;

    @KafkaListener(topics = "won-skin-upgrade-response-topic", groupId = "users-microservice")
    @Transactional
    public void getWonSkinFromUpgradeHandle(SendResultUpgradeToKafkaDto event) {
        try {
            UpgradeSession session = upgradeSessionRepository.findById(event.sessionId())
                    .orElseThrow(() -> new NotFound("Сессия Upgrade не найдена"));

            User user = userService.findUserById(event.userId());

            if (!session.getUserId().equals(user.getId())) {
                throw new BadRequest("В сообщении о Апгрейде, ID игрока, отличается от ID игрока, записанного в сессии апгрейда");
            }

            Set<SkinInst> sessionSkins = skinInstService.findAllById(new ArrayList<>(session.getPlayerSkinIds()));

            if (user.getInventory() == null || !user.getInventory().containsAllSkinsInst(sessionSkins)) {
                throw new BadRequest("У игрока либо нет инвентаря, либо нет каких то скинов из сессии апгрейда");
            }

            if (event.result().equals("WIN")) {
                log.info("Игрок {} выиграл скин в апгрейде (Result: {})", event.userId(), event.result());

                userWinWithUpgrade(user, sessionSkins, event.skin());

            } else if (event.result().equals("LOSE")) {
                log.info("Игрок {} проиграл скин(ы) в апгрейде (Result: {})", event.userId(), event.result());

                userLoseWithUpgrade(user, sessionSkins);

            } else {
                throw new BadRequest("Пришёл невалидный результат апгрейда");
            }

        } catch (Exception e) {
            log.error("Ошибка при выдаче скина (сторона слушателя)", e);
            throw e;
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void userWinWithUpgrade(User user, Set<SkinInst> skins, SendWonSkinToKafkaDto skinDto) {
        Inventory inventory = user.getInventory();

        for (SkinInst skin : skins) {
            skinInstService.deleteSkinInst(skin.getId());
        }

        SkinInst inst = new SkinInst();

        inst.setMarketPrice(skinDto.marketPrice());
        inst.setFullName(skinDto.fullName());
        inst.setRarity(skinDto.rarity());
        inst.setImageUrl(skinDto.imageUrl());

        inventory.addSkinInstToInventory(inst);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void userLoseWithUpgrade(User user, Set<SkinInst> skins) {
        for (SkinInst skin : skins) {
            skinInstService.deleteSkinInst(skin.getId());
        }
    }

}
