package org.example.microservice.entities.outbox;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "upgrade_sessions")
@ToString
@Getter
@Setter
@Slf4j

public class UpgradeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    // ID skins from USER-MICROSERVICE (SkinInst)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "session_player_skins", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "skin_id")
    @Size(max = 5)
    private Set<Long> playerSkinIds = new HashSet<>();

    // ID skin from MINIGAMES-MICROSERVICE (SkinCard)
    @NotNull
    private Long marketSkinId;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

}
