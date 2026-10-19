package org.example.microservice.repositories;

import org.example.microservice.entities.games.CaseGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CaseGameRepository extends JpaRepository<CaseGame, Long> {

    Optional<CaseGame> findByName(String name);

    List<CaseGame> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

}
