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
import org.example.microservice.enums.WeMod;
import org.example.microservice.enums.WeRarity;
import org.example.microservice.enums.WeType;
import org.example.microservice.enums.WeWear;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

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
    @Enumerated(EnumType.STRING)
    private WeType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WeRarity rarity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WeWear wear;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WeMod mod;

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

}
