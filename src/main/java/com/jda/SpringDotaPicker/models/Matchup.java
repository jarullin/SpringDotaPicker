package com.jda.SpringDotaPicker.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@IdClass(MatchupId.class)
public class Matchup{
    @Id
    private MatchupId id;
    private int gamesPlayed;
    private int wins;
}
