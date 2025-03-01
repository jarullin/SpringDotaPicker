package com.jda.SpringDotaPicker.services;

import com.jda.SpringDotaPicker.models.Hero;
import com.jda.SpringDotaPicker.models.Matchup;
import com.jda.SpringDotaPicker.repositories.HeroRepository;
import com.jda.SpringDotaPicker.repositories.MatchupsRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HeroService {
    private final HeroRepository heroRepository;
    private final MatchupsRepository matchupsRepository;

    public HeroService(HeroRepository heroRepository, MatchupsRepository matchupsRepository) {
        this.heroRepository = heroRepository;
        this.matchupsRepository = matchupsRepository;
    }

    /*
    Returns list of all heroes
     */
    public List<Hero> findAll() {
        Iterable<Hero> heroes = heroRepository.findAll();
        List<Hero> res = new ArrayList<>();
        heroes.iterator().forEachRemaining(res::add);
        res.sort(Comparator.comparing(Hero::getName));
        return res;
    }

    /*
    returns a single hero
     */
    public Hero findById(int id) {
        return heroRepository.findById(id);
    }

    /**
    * Calculates score for every unpicked hero
    * @param enemies: list of enemy heroes (up to five)
     * @return map (heroId: int , score: double), the higher is score - the more likely hero wins against given enemies
    */
    public Map<Integer, Double> calculatePick(List<Hero> enemies) {
        // TODO: add bans
        // heroId : calculated win probability
        Map<Integer, Double> pool = findAll().stream()
                .filter(x -> !enemies.contains(x))
                .collect(Collectors.toMap(Hero::getId, x -> 50.0));
        for (Hero enemy : enemies) {
            int maxGamesPlayed = matchupsRepository.findMaxGamesPlayedForEnemy(enemy.getId()).orElse(10000);
            int minGamesPlayed = matchupsRepository.findMaxGamesPlayedForEnemy(enemy.getId()).orElse(0);
            // if hero rarely picked against given enemy, the score will not changed
            // currently only top 50% picked heroes are taken into account
            double threshold = minGamesPlayed + (double) (maxGamesPlayed - minGamesPlayed) / 2;
            for (Integer heroId : pool.keySet()) {
                Matchup matchup = matchupsRepository.findById_HeroIdAndId_EnemyId(heroId, enemy.getId());
                assert matchup != null;
                if (matchup.getGamesPlayed() >= threshold) {
                    // 20% of winrate/loserate are added to score
                    pool.compute(heroId, (x, v) -> v + 0.2 * (matchup.getWins() - 50));
                }
            }
        }
        return pool;
    }

    /**
     *
     * @param enemies: list of enemies
     * @return top 5 carry picks against given enemy
     */
    public List<Hero> getCarryPicks(List<Hero> enemies) {
        return calculatePick(enemies).entrySet().stream()
                .sorted(Map.Entry.<Integer,Double>comparingByValue().reversed())
                .map(entry -> findById(entry.getKey()))
                .limit(5)
                .collect(Collectors.toList());
    }
}
