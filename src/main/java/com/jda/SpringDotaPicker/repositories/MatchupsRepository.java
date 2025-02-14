package com.jda.SpringDotaPicker.repositories;

import com.jda.SpringDotaPicker.models.Matchup;
import com.jda.SpringDotaPicker.models.MatchupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchupsRepository extends JpaRepository<Matchup, MatchupId> {
    Matchup findOne(int heroId, int enemyId);

    @Query("Select Min(m.gamesPlayed) from Matchup m where m.id.enemyId = :enemyId")
    Optional<Integer> findMinGamesPlayedForEnemy(@Param("enemyId") Integer enemyId);
    @Query("Select Max(m.gamesPlayed) from Matchup m where m.id.enemyId = :enemyId")
    Optional<Integer> findMaxGamesPlayedForEnemy(@Param("enemyId") Integer enemyId);
}
