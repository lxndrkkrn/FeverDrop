package org.example.microservice.repositories;

import org.example.microservice.entities.Skin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SkinRepository extends JpaRepository<Skin, Long> {

    Optional<Skin> findSkinBySkinMarketId(String skinMarketId);

    List<Skin> findSkinsByMarketPriceBetween(BigDecimal min, BigDecimal max);

}
