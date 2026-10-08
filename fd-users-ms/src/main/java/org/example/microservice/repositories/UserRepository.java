package org.example.microservice.repositories;

import org.example.microservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUuid(UUID uuid);

    Optional<User> findUserByEmail(String email);

}
