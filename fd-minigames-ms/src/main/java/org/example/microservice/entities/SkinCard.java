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

@Entity
@Table(name = "skin_cards")
@ToString
@Getter
@Setter
@Slf4j

public class SkinCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String fullName;

    @Positive
    private BigDecimal marketPrice;

    @URL
    private String imageUrl;

    private String rarity;

}
