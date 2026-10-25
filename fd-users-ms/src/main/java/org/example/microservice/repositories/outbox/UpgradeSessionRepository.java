package org.example.microservice.repositories.outbox;

import org.example.microservice.entities.outbox.UpgradeSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpgradeSessionRepository extends JpaRepository<UpgradeSession, Long> {
}
