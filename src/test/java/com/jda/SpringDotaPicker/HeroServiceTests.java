package com.jda.SpringDotaPicker;

import com.jda.SpringDotaPicker.models.*;
import com.jda.SpringDotaPicker.repositories.HeroesRepository;
import com.jda.SpringDotaPicker.repositories.MatchupsRepository;
import com.jda.SpringDotaPicker.repositories.RoleRepository;
import com.jda.SpringDotaPicker.services.HeroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HeroServiceTests {
    @Mock
    private MatchupsRepository matchupsRepository;

    @Mock
    private HeroesRepository heroRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private HeroService heroService;

    private Hero hero1;
    private Hero hero2;
    private Hero hero3;

    @BeforeEach
    void setUp() {
        hero1 = new Hero();
        hero1.setId(1);
        hero1.setName("Hero 1");

        hero2 = new Hero();
        hero2.setId(2);
        hero2.setName("Hero 2");

        hero3 = new Hero();
        hero3.setId(3);
        hero3.setName("Hero 3");
    }
    @Test
    void checkAlgorithmCorrectness(){
        //Enemy picked a hero #1
        List<Hero> enemies = List.of(hero1);
        List<Hero> allHeroes = List.of(hero1, hero2, hero3);

        //Hero #2 won 75/100 matches against hero 1
        Matchup matchup1 = new Matchup(2, 1);
        matchup1.setGamesPlayed(100);
        matchup1.setWins(75);
        when(matchupsRepository.findById_HeroIdAndId_EnemyId(2, 1)).thenReturn(Optional.of(matchup1));

        //Hero #3 won 25/100 matches against hero 1
        Matchup matchup2 = new Matchup(3, 1);
        matchup2.setGamesPlayed(100);
        matchup2.setWins(25);
        when(matchupsRepository.findById_HeroIdAndId_EnemyId(3, 1)).thenReturn(Optional.of(matchup2));

        when(heroRepository.findAll()).thenReturn(allHeroes);

        // TODO: set test for calculating threshold
        when(matchupsRepository.findMaxGamesPlayedForEnemy(1)).thenReturn(Optional.of(100));
        when(matchupsRepository.findMinGamesPlayedForEnemy(1)).thenReturn(Optional.of(0));

        Map<Integer, Double> result = heroService.calculatePick(enemies);
        assertEquals(75.0, result.get(2)); // (25% winrate - 50%) * 1 + 50 = 45.0
        assertEquals(25.0, result.get(3)); // (75% winrate - 50%) * 1 + 50 = 65.0
    }
}
