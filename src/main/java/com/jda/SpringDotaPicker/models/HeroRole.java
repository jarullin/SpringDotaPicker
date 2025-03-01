package com.jda.SpringDotaPicker.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class HeroRole {

    @EmbeddedId
    private HeroRoleId id;

    public enum Role {
        CARRY, MIDLANER, OFFLANER, SOFTSUP, HARDSUP
    }
}
