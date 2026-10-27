package org.example.microservice.repositories;

import org.example.microservice.entities.SkinCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SkinCardRepository extends JpaRepository<SkinCard, Long> {

    Optional<SkinCard> findByFullName(String fullName);

    List<SkinCard> findAllByFullNameIn(List<String> fullNames);

    List<SkinCard> findSkinsByMarketPriceBetween(BigDecimal min, BigDecimal max);

    @Query(value = """
        SELECT * FROM skin_cards 
        ORDER BY ABS(market_price - :targetPrice) ASC 
        LIMIT 1
        """, nativeQuery = true)
    Optional<SkinCard> findClosestByPrice(@Param("targetPrice") BigDecimal targetPrice);

}
