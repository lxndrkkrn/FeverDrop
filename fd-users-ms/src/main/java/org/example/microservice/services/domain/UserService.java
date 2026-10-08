package org.example.microservice.services.domain;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.domain.client.NotFound;
import org.example.microservice.entities.User;
import org.example.microservice.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor

public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public User createUser(String email, String password, String tradeUrl) {

        User user = new User();

        user.setEmail(email);
        user.setEncodePassword(passwordEncoder.encode(password));
        user.setTradeUrl(tradeUrl);

        userRepository.save(user);

        return user;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public User changeEmail(Long id, String email) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFound("Игрок не найден"));

        user.setEmail(email);

        userRepository.save(user);

        return user;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public User changePassword(Long id, String password) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFound("Игрок не найден"));

        user.setEncodePassword(passwordEncoder.encode(password));

        userRepository.save(user);

        return user;
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFound("Игрок не найден"));
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public User findUserByUuid(UUID uuid) {
        return userRepository.findUserByUuid(uuid)
                .orElseThrow(() -> new NotFound("Игрок не найден"));
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFound("Игрок не найден"));
    }

}
