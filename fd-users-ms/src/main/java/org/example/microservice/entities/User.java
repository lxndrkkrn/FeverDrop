package org.example.microservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "users")
@ToString
@Getter
@Setter
@Slf4j

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private UUID uuid;

    @Version
    private Long version;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    private String encodePassword;

    @NotNull
    @URL
    private String tradeUrl;

    @NotNull
    @PositiveOrZero
    private BigDecimal balance = BigDecimal.ZERO;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private org.example.microservice.entities.Inventory inventory;

    @PrePersist
    protected void onCreate() { if (this.uuid == null) this.uuid = UUID.randomUUID(); }

    public void initializeAccount(org.example.microservice.entities.Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Инвентарь не могут быть null");
        }

        this.inventory = inventory;
        inventory.setUser(this);
    }

    public void addBalance(BigDecimal delta) {
        this.balance = this.balance.add(delta);
        log.info("Игроку {} добавлено на баланс {}. Новый баланс: {}", this.id, delta, this.balance);
    }

    public void takeBalance(BigDecimal delta) {
        this.balance = this.balance.subtract(delta);
        log.info("Игроку {} вычтино с баланса {}. Новый баланс: {}", this.id, delta, this.balance);
    }

}
