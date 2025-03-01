package com.jda.SpringDotaPicker.services;

import com.jda.SpringDotaPicker.models.Hero;
import com.jda.SpringDotaPicker.models.HeroRole;
import com.jda.SpringDotaPicker.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;

@Service
@EnableAsync
public class RolesUpdateService {
    private final RoleRepository roleRepository;
    private final HeroService heroService;

    public RolesUpdateService(RoleRepository roleRepository, HeroService heroService) {
        this.roleRepository = roleRepository;
        this.heroService = heroService;
    }

    /*
    updates roles in the database once a week
     */
    @Async
    @Transactional
    @Scheduled(cron = "0 0 0 * * 0")
    public void updateRoles() throws IOException {
        List<Hero> heroes = heroService.findAll();
        for (Hero hero : heroes) {
            roleRepository.deleteById_HeroId(hero.getId());
            URL url = URI.create("https://dota2protracker.com/hero/" + hero.getName()).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            if (code != 200) {
                System.out.println("Error: " + connection.getResponseMessage());
                return;
            }
            StringBuilder response = getResponse(connection);
            List<String> heroRoles = parseJson(response);
            for (String role : heroRoles) {
                HeroRole heroRole = new HeroRole();
                heroRole.getId().setRole(role);
                heroRole.getId().setHeroId(hero.getId());
                roleRepository.save(heroRole);
            }

            System.out.println("Roles updated: " + hero.getName());
            try {
                Thread.sleep(1000);     // So we don't get the site DOSed
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private StringBuilder getResponse(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        StringBuilder sb = new StringBuilder(content.toString());
        int start = sb.indexOf("hero_stats") + 11;
        sb.delete(0, start);
        int end = sb.indexOf("]") + 1;
        sb.delete(end, sb.length() - 1);
        return sb;
    }

    /*
    Parses json placed on the HTML code in dota2protracker
    Criterion for a role assigned to hero: 20% or more of total games played on the role
     */
    private List<String> parseJson(StringBuilder sb) {
        int index = sb.indexOf("position");
        List<String> roles = new ArrayList<>();
        Map<Integer, Integer> posToMatches = new HashMap<>();
        int totalMatches = 0;
        while (index != -1) {
            sb.delete(0, index + 10);
            if (sb.indexOf("pos 5") == 0) {
                sb.delete(0, 19);
                int matches = Integer.parseInt(sb.substring(0, sb.indexOf("winrate") - 1));
                posToMatches.put(5, matches);
                totalMatches += matches;
                sb.delete(0, sb.indexOf("winrate") + 8);
            } else if (sb.indexOf("pos 4") == 0) {
                sb.delete(0, 19);
                int matches = Integer.parseInt(sb.substring(0, sb.indexOf("winrate") - 1));
                posToMatches.put(4, matches);
                totalMatches += matches;
                sb.delete(0, sb.indexOf("winrate") + 8);

            } else if (sb.indexOf("pos 3") == 0) {
                sb.delete(0, 19);
                int matches = Integer.parseInt(sb.substring(0, sb.indexOf("winrate") - 1));
                posToMatches.put(3, matches);
                totalMatches += matches;
                sb.delete(0, sb.indexOf("winrate") + 8);

            } else if (sb.indexOf("pos 2") == 0) {
                sb.delete(0, 19);
                int matches = Integer.parseInt(sb.substring(0, sb.indexOf("winrate") - 1));
                posToMatches.put(2, matches);
                totalMatches += matches;
                sb.delete(0, sb.indexOf("winrate") + 8);

            } else if (sb.indexOf("pos 1") == 0) {
                sb.delete(0, 19);
                int matches = Integer.parseInt(sb.substring(0, sb.indexOf("winrate") - 1));
                posToMatches.put(1, matches);
                totalMatches += matches;
                sb.delete(0, sb.indexOf("winrate") + 8);

            }
            index = sb.indexOf("position");
        }
        for (Map.Entry<Integer, Integer> entry : posToMatches.entrySet()) {
            if (entry.getValue() >= totalMatches * 0.2) {
                switch (entry.getKey()) {
                    case 1:
                        roles.add(HeroRole.Role.CARRY.toString());
                        break;
                    case 2:
                        roles.add(HeroRole.Role.MIDLANER.toString());
                        break;
                    case 3:
                        roles.add(HeroRole.Role.OFFLANER.toString());
                        break;
                    case 4:
                        roles.add(HeroRole.Role.SOFTSUP.toString());
                        break;
                    case 5:
                        roles.add(HeroRole.Role.HARDSUP.toString());
                        break;
                    default:
                        break;
                }
            }
        }
        return roles;
    }

}
