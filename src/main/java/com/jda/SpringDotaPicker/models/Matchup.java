package com.jda.SpringDotaPicker.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@IdClass(MatchupId.class)
public class Matchup{
    @Id
    private int heroId;
    @Id
    private int enemyId;
    private int gamesPlayed;
    private int wins;
}
