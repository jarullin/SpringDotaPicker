package com.jda.SpringDotaPicker.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class HeroRole {

    @Id
    private Integer heroId;

    @Id
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        CARRY, MIDLANER, OFFLANER, SOFTSUP, HARDSUP
    }
}
