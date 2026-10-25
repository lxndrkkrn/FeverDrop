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
@ToString(exclude = "skinInstList")
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
    private User user;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SkinInst> skinInstList = new ArrayList<>();

    @PrePersist
    protected void onCreate() { if (this.uuid == null) this.uuid = UUID.randomUUID(); }

    public List<SkinInst> addSkinInstToInventory(SkinInst skin) {
        this.skinInstList.add(skin);
        skin.setInventory(this);

        return this.skinInstList;
    }

    public List<SkinInst> takeSkinInstFromInventory(SkinInst skin) {
        this.skinInstList.remove(skin);
        skin.setInventory(null);

        return this.skinInstList;
    }

    public boolean containsAllSkinsInst(Set<SkinInst> list) {
        return new HashSet<>(this.skinInstList).containsAll(list);
    }

}
