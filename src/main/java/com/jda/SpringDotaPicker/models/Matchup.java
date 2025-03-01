package com.jda.SpringDotaPicker.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Matchup{
    @EmbeddedId
    private MatchupId id;
    private int gamesPlayed;
    private int wins;
}
