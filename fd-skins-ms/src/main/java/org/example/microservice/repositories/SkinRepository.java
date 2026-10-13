package org.example.microservice.repositories;

import org.example.microservice.entities.Skin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories

public interface SkinRepository extends JpaRepository<Skin, Long> {

    Optional<Skin> findSkinBySkinMarketId(String skinMarketId);

    List<Skin> findSkinsByMarketPriceBetween(BigDecimal min, BigDecimal max);

    @Modifying
    @Query("DELETE FROM Skin s WHERE s.lastSeenAt < :startTime")
    int deleteObsoleteSkins(@Param("startTime") LocalDateTime startTime);

}
