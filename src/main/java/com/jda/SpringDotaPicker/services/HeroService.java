package com.jda.SpringDotaPicker.services;

import com.jda.SpringDotaPicker.models.Hero;
import com.jda.SpringDotaPicker.models.HeroPick;
import com.jda.SpringDotaPicker.models.Matchup;
import com.jda.SpringDotaPicker.repositories.HeroesRepository;
import com.jda.SpringDotaPicker.repositories.MatchupsRepository;
import com.jda.SpringDotaPicker.repositories.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HeroService {
    private final HeroesRepository heroesRepository;
    private final MatchupsRepository matchupsRepository;
    private final RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(HeroService.class);

    public HeroService(HeroesRepository heroesRepository, MatchupsRepository matchupsRepository, RoleRepository roleRepository) {
        this.heroesRepository = heroesRepository;
        this.matchupsRepository = matchupsRepository;
        this.roleRepository = roleRepository;
    }

    /*
    Returns list of all heroes
     */
    public List<Hero> findAll() {
        Iterable<Hero> heroes = heroesRepository.findAll();
        List<Hero> res = new ArrayList<>();
        heroes.iterator().forEachRemaining(res::add);
        res.sort(Comparator.comparing(Hero::getName));
        return res;
//        return res.stream().map(hero -> {
//            List<String> roles = roleRepository.findById_HeroId(hero.getId())
//                    .stream()
//                    .map(heroRole -> heroRole.getId().getRole())
//                    .collect(Collectors.toList());
//            hero.setRoles(roles);
//            return hero;
//        }).collect(Collectors.toList());
    }

    /*
    returns a single hero
     */
    public Hero findById(int id) {
        return heroesRepository.findById(id).orElseThrow(() -> new RuntimeException("Hero not found, id=" + id));
    }

    /**
     * Calculates score for every unpicked hero
     *
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
            int minGamesPlayed = matchupsRepository.findMinGamesPlayedForEnemy(enemy.getId()).orElse(0);
            // if hero rarely picked against given enemy, the score will not changed
            // currently only top 50% picked heroes are taken into account
            double threshold = minGamesPlayed + (maxGamesPlayed - minGamesPlayed) / 4.0;
            pool.forEach((heroId, score) -> {
                matchupsRepository.findById_HeroIdAndId_EnemyId(heroId, enemy.getId()).ifPresent(matchup -> {
                    if (matchup.getGamesPlayed() >= threshold) {
                        // 20% of winrate/loserate are added to score
                        double winrateAgainst = ((double) matchup.getWins() / matchup.getGamesPlayed()) * 100;
                        double delta = 1 * (winrateAgainst - 50);
                        pool.put(heroId, score + delta);
                    }
                });

            });
        }
        return pool;
    }

    /**
     * @param enemies: list of enemies
     * @param role: role for filter
     * @return top 5 carry picks for 5 roles against given enemy
     */
    public List<HeroPick> getRolePicks(List<Hero> enemies, String role) {
        return calculatePick(enemies)
               .entrySet()
               .stream()
               .filter(entry -> roleRepository
                       .findById_HeroId(entry.getKey()).stream()
                       .anyMatch(r -> r.getId().getRole().equals(role))
               )
               .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
               .limit(5)
               .map(entry -> new HeroPick(findById(entry.getKey()), entry.getValue()))
               .collect(Collectors.toList());
    }


    /**
     * @param enemies: list of enemies
     * @param bans: list of banned heroes
     * @param role: role for filter
     * @return top 5 picks for selected role against given enemy
     */
    public List<HeroPick> getRolePicks(List<Hero> enemies, List<Hero> bans, String role) {
        return applyBans(calculatePick(enemies), bans)
                .entrySet()
                .stream()
                .filter(entry -> roleRepository
                        .findById_HeroId(entry.getKey()).stream()
                        .anyMatch(r -> r.getId().getRole().equals(role))
                )
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(5)
                .map(entry -> new HeroPick(findById(entry.getKey()), entry.getValue()))
                .collect(Collectors.toList());
    }


    private Map<Integer, Double> applyBans(Map<Integer, Double> pool, List<Hero> bans) {
        for(Hero ban : bans) {
            int maxGamesPlayed = matchupsRepository.findMaxGamesPlayedForEnemy(ban.getId()).orElse(10000);
            int minGamesPlayed = matchupsRepository.findMinGamesPlayedForEnemy(ban.getId()).orElse(0);
            // if hero rarely picked against given enemy, the score will not changed
            // currently only top 50% picked heroes are taken into account
            double threshold = minGamesPlayed + (double) (maxGamesPlayed - minGamesPlayed) / 2;
            for (Integer heroId : pool.keySet()) {
                Matchup matchup = matchupsRepository.findById_HeroIdAndId_EnemyId(heroId, ban.getId()).orElse(null);
                try {
                    if (matchup != null & matchup.getGamesPlayed() >= threshold) {
                        // 20% of winrate/loserate are added to score
                        double winrateAgainst = ((double) matchup.getWins() / matchup.getGamesPlayed()) * 100;
                        double delta = 2 * (- winrateAgainst - 50);
                        pool.compute(heroId, (key, value) -> value + delta);
                    }
                } catch (Exception e) {
                    logger.error("Error while looking for matchup: heroId=" + heroId + ", enemyId=" + ban.getId());
                    System.out.println("Error while looking for matchup: heroId=" + heroId + ", enemyId=" + ban.getId());
                }
            }
        }
        return pool;
    }
}
