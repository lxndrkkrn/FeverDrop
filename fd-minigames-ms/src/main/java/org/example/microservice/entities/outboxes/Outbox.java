package org.example.microservice.entities.outboxes;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "outboxes_minigames")
@ToString
@Getter
@Setter
@Slf4j

public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String aggregateType;

    @NotNull
    private String EventType;

    @NotNull
    private Object payload;

}
