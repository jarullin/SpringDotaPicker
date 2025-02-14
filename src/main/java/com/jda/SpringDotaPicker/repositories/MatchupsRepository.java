package com.jda.SpringDotaPicker.repositories;

import com.jda.SpringDotaPicker.models.Matchup;
import com.jda.SpringDotaPicker.models.MatchupId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchupsRepository extends CrudRepository<Matchup, MatchupId> {
    Matchup findOne(int heroId, int enemyId);

    @Query("Select Min(games_played) from matchups where enemy_id = ?1")
    int findMinGamesPlayedForEnemy(int heroId);
    @Query("Select Max(games_played) from matchups where enemy_id = ?1")
    int findMaxGamesPlayedForEnemy(int heroId);
}
