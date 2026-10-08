package org.example.microservice.services.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.dtos.request.balance.AddBalanceRequestDto;
import org.example.microservice.dtos.request.balance.TakeBalanceRequestDto;
import org.example.microservice.entities.User;
import org.example.microservice.repositories.UserRepository;
import org.example.microservice.services.domain.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Slf4j

public class BalanceAppService {

    private final UserService userService;

    private final UserRepository userRepository;

    @Transactional
    public void addBalance(@Validated AddBalanceRequestDto dto) {
        log.info("Попытка добавления {} на баланс игроку {}", dto.delta(), dto.id());

        User user = userService.findUserById(dto.id());

        user.addBalance(dto.delta());

        userRepository.save(user);
        log.info("Добавлено {} на баланс игроку {}", dto.delta(), dto.id());
    }

    @Transactional
    public void takeBalance(@Validated TakeBalanceRequestDto dto) {
        log.info("Попытка снятия {} с баланса игрока {}", dto.delta(), dto.id());

        User user = userService.findUserById(dto.id());

        user.takeBalance(dto.delta());

        userRepository.save(user);
        log.info("Снято {} с баланса игрока {}", dto.delta(), dto.id());
    }
}
