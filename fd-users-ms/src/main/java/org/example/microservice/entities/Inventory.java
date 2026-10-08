package org.example.microservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Entity
@Table(name = "inventories")
@ToString
@Getter
@Setter
@Slf4j

public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private UUID uuid;

    @Version
    private Long version;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private org.example.microservice.entities.User user;

    @ManyToMany()
    @JoinTable(name = "inventory_skins",
                joinColumns = @JoinColumn(name = "inventory_id"),
                inverseJoinColumns = @JoinColumn(name = "skin_id")
    )
    private List<org.example.microservice.entities.SkinId> skins = new ArrayList<>();

    @PrePersist
    protected void onCreate() { if (this.uuid == null) this.uuid = UUID.randomUUID(); }

}
