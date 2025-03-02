package com.jda.SpringDotaPicker.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="hero_roles")
public class HeroRole {

    @EmbeddedId
    private HeroRoleId id;

    public enum Role {
        CARRY, MIDLANER, OFFLANER, SOFTSUP, HARDSUP
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HeroRole) {
            return id.equals(((HeroRole)obj).id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
