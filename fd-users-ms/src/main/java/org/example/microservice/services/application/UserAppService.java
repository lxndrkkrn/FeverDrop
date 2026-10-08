package org.example.microservice.services.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.dtos.request.user.ChangeUserEmailRequestDto;
import org.example.microservice.dtos.request.user.ChangeUserPasswordRequestDto;
import org.example.microservice.dtos.request.user.CreateUserRequestDto;
import org.example.microservice.dtos.request.user.DeleteUserRequestDto;
import org.example.microservice.dtos.response.user.CreateUserResponseDto;
import org.example.microservice.entities.Inventory;
import org.example.microservice.entities.User;
import org.example.microservice.services.domain.InventoryService;
import org.example.microservice.services.domain.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Slf4j

public class UserAppService {

    private final UserService userService;
    private final InventoryService inventoryService;

    @Transactional
    public CreateUserResponseDto createUser(@Validated CreateUserRequestDto dto) {
        log.info("Попытка создания пользователя...");

        User user = userService.createUser(dto.email(), dto.password(), dto.tradeUrl());

        Inventory inventory = inventoryService.createInventory(user);

        user.initializeAccount(inventory);

        log.info("Чистый пользователь создан, ID: {}", user.getId());

        return new CreateUserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getTradeUrl(),
                inventory.getId()
        );
    }

    @Transactional
    public void deleteUser(@Validated DeleteUserRequestDto dto) {
        log.info("Попытка удаления пользователя...");

        userService.deleteUser(dto.id());

        log.info("Пользователь удалён");
    }

    @Transactional
    public void changeEmail(@Validated ChangeUserEmailRequestDto dto) {
        log.info("Попытка сменить Email игроку с ID: {}", dto.id());

        userService.changeEmail(dto.id(), dto.email());

        log.info("Email игроку {} сменён", dto.id());
    }

    @Transactional
    public void changePassword(@Validated ChangeUserPasswordRequestDto dto) {
        log.info("Попытка сменить Password игроку с ID: {}", dto.id());

        userService.changePassword(dto.id(), dto.password());

        log.info("Password игроку {} сменён", dto.id());
    }

}
