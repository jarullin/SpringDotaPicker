package com.jda.SpringDotaPicker.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="matchups")
public class Matchup{
    @EmbeddedId
    private MatchupId id;
    private int gamesPlayed;
    private int wins;

    public Matchup() {}

    public Matchup(int heroId, int enemyId) {
        this.id = new MatchupId(heroId,enemyId);
    }
}
