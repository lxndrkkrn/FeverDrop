package org.example.microservice.repositories;

import org.example.microservice.entities.SkinCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SkinCardRepository extends JpaRepository<SkinCard, Long> {

    Optional<SkinCard> findByFullName(String fullName);

    List<SkinCard> findAllByFullNameIn(List<String> fullNames);

    List<SkinCard> findSkinsByMarketPriceBetween(BigDecimal min, BigDecimal max);

}
