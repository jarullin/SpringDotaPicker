package com.jda.SpringDotaPicker.controllers;

import com.jda.SpringDotaPicker.models.Hero;
import com.jda.SpringDotaPicker.models.HeroRole;
import com.jda.SpringDotaPicker.services.HeroService;
import org.hibernate.boot.jaxb.SourceType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class APIController {


    private final HeroService heroService;

    public APIController(HeroService heroService) {
        this.heroService = heroService;
    }

    @GetMapping("/heroes")
    public List<Hero> heroes() {
        return heroService.findAll();
    }

    @GetMapping("/picks")
    public PickResponse picks(@RequestParam(required = true) List<Integer> enemies) {
        assert enemies != null;
        assert !enemies.isEmpty() && enemies.size() <= 5;
        List<Hero> enemyHeroes = heroService.findAll()
                .stream()
                .filter(hero -> enemies.contains(hero.getId()))
                .toList();
        PickResponse response = new PickResponse(heroService.getRolePicks(enemyHeroes, HeroRole.Role.CARRY.name()),
                heroService.getRolePicks(enemyHeroes, HeroRole.Role.MIDLANER.name()),
                heroService.getRolePicks(enemyHeroes, HeroRole.Role.OFFLANER.name()),
                heroService.getRolePicks(enemyHeroes, HeroRole.Role.SOFTSUP.name()),
                heroService.getRolePicks(enemyHeroes, HeroRole.Role.HARDSUP.name())
                );
        System.out.println(response);
        return response;
    }

    public record PickResponse(List<Hero> carry,
                               List<Hero> mid,
                               List<Hero> offlane,
                               List<Hero> softsup,
                               List<Hero> hardsup) {

    }
}
