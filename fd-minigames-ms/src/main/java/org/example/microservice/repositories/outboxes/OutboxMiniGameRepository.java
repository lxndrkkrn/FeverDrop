package org.example.microservice.repositories.outboxes;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.example.microservice.entities.outboxes.OutboxMiniGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxMiniGameRepository extends JpaRepository<OutboxMiniGame, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "-2")}) // SKIP LOCKED
    @Query("SELECT o FROM OutboxMiniGame o WHERE o.aggregateType = :aggregateType AND o.eventType IN :eventTypes")
    List<OutboxMiniGame> findAllForProcessing(
            @Param("aggregateType") String aggregateType,
            @Param("eventTypes") List<String> eventTypes
    );

}
