package org.example.microservice.entities.games;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.example.microservice.entities.SkinCard;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cases")
@ToString(exclude = "skins")
@Getter
@Setter
@Slf4j

public class CaseGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String category;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    @URL
    private String imageUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "case_items", // Имя промежуточной таблицы в БД
            joinColumns = @JoinColumn(name = "case_id"),
            inverseJoinColumns = @JoinColumn(name = "skin_card_id")
    )
    private List<SkinCard> skins = new ArrayList<>();

    @Version
    private Long version;

    public void addSkin(SkinCard skin) {
        if (this.skins == null) {
            this.skins = new ArrayList<>();
        }
        if (!this.skins.contains(skin)) {
            this.skins.add(skin);
        }
    }

    public void removeSkin(SkinCard skin) {
        if (this.skins != null) {
            this.skins.remove(skin);
        }
    }

}
