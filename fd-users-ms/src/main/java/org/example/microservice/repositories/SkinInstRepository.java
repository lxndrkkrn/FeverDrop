package org.example.microservice.repositories;

import org.example.microservice.entities.SkinInst;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;


public interface SkinInstRepository extends JpaRepository<SkinInst, Long> {

    List<SkinInst> findAllByInventoryIdAndSkinMarketId(Long id, String skinMarketId);

    List<SkinInst> findAllByFullName(String fullName);

    List<SkinInst> findSkinsByMarketPriceBetween(BigDecimal min, BigDecimal max);

}
