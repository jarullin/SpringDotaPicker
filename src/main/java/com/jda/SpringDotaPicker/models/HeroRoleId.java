package com.jda.SpringDotaPicker.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;


@Data
@Embeddable
public class HeroRoleId implements Serializable {
    private int heroId;
    private String role;

    public HeroRoleId() {}
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HeroRoleId) {
            return heroId == ((HeroRoleId) obj).heroId && role.equals(((HeroRoleId) obj).role);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(heroId, role);
    }
}
