package org.example.microservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Table(name = "skins")
@ToString
@Getter
@Setter
@Slf4j

public class Skin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String skinMarketId;

    @NotNull
    private String name;

    @NotNull
    //@Enumerated(EnumType.STRING)
    private String type;

    @NotNull
    //@Enumerated(EnumType.STRING)
    private String rarity;

    @NotNull
    //@Enumerated(EnumType.STRING)
    private String wear;

    @NotNull
    //@Enumerated(EnumType.STRING)
    private String mod;

    @NotNull
    @Max(value = 1)
    @Min(value = 0)
    private Float skinFloat;

    //@NotNull
    @Positive
    private BigDecimal steamPrice;

    @NotNull
    @Positive
    private BigDecimal marketPrice;

    @NotNull
    @URL
    private String imageUrl;

    @Version
    private Long version;

    private LocalDateTime atLastSeen;

    public void updatePriceFromMarket(BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Цена маркета должна быть положительной");
        }
        this.marketPrice = newPrice;
    }

}
