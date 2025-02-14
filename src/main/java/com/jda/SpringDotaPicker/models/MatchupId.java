package com.jda.SpringDotaPicker.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class MatchupId implements Serializable {

    private int heroId;
    private int enemyId;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MatchupId) {
            MatchupId other = (MatchupId) obj;
            return other.getHeroId() == this.getHeroId() & other.getEnemyId() == this.getEnemyId();
        }
        return false;
    }
}
