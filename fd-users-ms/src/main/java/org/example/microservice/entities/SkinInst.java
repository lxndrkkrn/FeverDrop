package org.example.microservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "skin_instances")
@ToString(exclude = "inventory")
@Getter
@Setter
@Slf4j

public class SkinInst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String fullName;

    private String skinMarketId;

    @Positive
    private BigDecimal marketPrice;

    @URL
    private String imageUrl;

    private String rarity;

    @NotNull
    private LocalDateTime getTime = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @Column(name = "is_frozen")
    private boolean isFrozen = false;

    @Version
    private Long version;

}
