package org.example.microservice.repositories;

import org.example.microservice.entities.SkinId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkinIdRepository extends JpaRepository<SkinId, Long> {

    Optional<SkinId> findSkinIdBySkinId(Long skinId);

}
