package com.jda.SpringDotaPicker.services;

import com.jda.SpringDotaPicker.models.Hero;
import com.jda.SpringDotaPicker.models.HeroRole;
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

    public List<Hero> findAll() {
        Iterable<Hero> heroes = heroRepository.findAll();
        List<Hero> res = new ArrayList<>();
        heroes.iterator().forEachRemaining(res::add);
        res.sort(Comparator.comparing(Hero::getName));
        return res;
    }

    public Hero findById(int id) {
        return heroRepository.findById(id);
    }

    public void updateRoles(int heroid, List<HeroRole> roles){

    }

    public Map<Integer, Double> calculatePick(List<Hero> enemies) {
        // TODO: add bans
        // heroId : calculated win probability
        Map<Integer, Double> pool = findAll().stream()
                .filter(x -> !enemies.contains(x))
                .collect(Collectors.toMap(Hero::getId, _ -> 50.0));
        for (Hero enemy : enemies) {
            int maxGamesPlayed = matchupsRepository.findMaxGamesPlayedForEnemy(enemy.getId());
            int minGamesPlayed = matchupsRepository.findMaxGamesPlayedForEnemy(enemy.getId());
            double threshold = minGamesPlayed + (double) (maxGamesPlayed - minGamesPlayed) / 2;
            for (Integer heroId : pool.keySet()) {
                Matchup matchup = matchupsRepository.findOne(heroId, enemy.getId());
                if (matchup != null & matchup.getGamesPlayed() >= threshold) {
                    pool.compute(heroId, (k, v) -> v + 0.2 * (matchup.getWins() - 50));
                }
            }
        }
        return pool;
    }

    public List<Hero> getCarryPicks(List<Hero> enemies) {
        return calculatePick(enemies).entrySet().stream()
                .sorted(Map.Entry.<Integer,Double>comparingByValue().reversed())
                .map(entry -> findById(entry.getKey()))
                .limit(5)
                .collect(Collectors.toList());

    }
}
