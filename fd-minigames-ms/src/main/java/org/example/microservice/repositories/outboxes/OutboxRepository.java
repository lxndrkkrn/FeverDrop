package org.example.microservice.repositories.outboxes;

import org.example.microservice.entities.outboxes.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {
}
