package com.jda.SpringDotaPicker.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jda.SpringDotaPicker.models.Hero;
import com.jda.SpringDotaPicker.models.Matchup;
import com.jda.SpringDotaPicker.repositories.MatchupsRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

@Service
public class MatchupsUpdateService {
    private static final String BASE_URL = "https://api.opendota.com/api/heroes/";
    private final HeroService heroService;
    private final MatchupsRepository matchupsRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MatchupsUpdateService(HeroService heroService, MatchupsRepository matchupsRepository) {
        this.heroService = heroService;
        this.matchupsRepository = matchupsRepository;
    }

    @Async
    @Transactional
    @Scheduled(cron = "0 0 0 * * 0")
    public void updateMatchups() {
        System.out.println("Updating Matchups...");
        List<Hero> heroes = heroService.findAll();

        for (Hero hero : heroes) {
            System.out.println("Requesting matchup for " + hero.getName());

            String response = fetchMatchupData(hero.getId());
            if (response == null) {
                System.out.println("Failed to update matchup for " + hero.getName());
                continue;
            }

            processMatchupData(hero, response);

            try {
                Thread.sleep(1000);  // OpenDota limit: 100 requests/min
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", e);
            }
        }
    }

    private String fetchMatchupData(int heroId) {
        String urlString = BASE_URL + heroId + "/matchups";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != 200) {
                return null;
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                return content.toString();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching matchups for hero " + heroId, e);
        }
    }

    private void processMatchupData(Hero hero, String jsonData) {
        try {
            JsonNode root = objectMapper.readTree(jsonData);
            int counter = 0;
            for (JsonNode node : root) {
                System.out.print("\r" + hero.getName() + " :: Saving matchup #" + counter);

                int enemyId = node.get("hero_id").asInt();
                Matchup matchup = matchupsRepository.findById_HeroIdAndId_EnemyId(hero.getId(), enemyId)
                        .orElse(new Matchup(hero.getId(), enemyId));

                matchup.setWins(node.get("wins").asInt());
                matchup.setGamesPlayed(node.get("games_played").asInt());
                matchupsRepository.save(matchup);
                counter++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing matchups for hero " + hero.getName(), e);
        }
    }
}
