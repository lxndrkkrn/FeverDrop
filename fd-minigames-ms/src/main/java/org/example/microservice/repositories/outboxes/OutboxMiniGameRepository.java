package org.example.microservice.repositories.outboxes;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.example.microservice.entities.outboxes.OutboxMiniGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

public interface OutboxMiniGameRepository extends JpaRepository<OutboxMiniGame, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "-2")}) // SKIP LOCKED
    @Query("SELECT o FROM OutboxMiniGame o") // Забираем вообще все записи из таблицы
    List<OutboxMiniGame> findAllForProcessing();

}
