package com.jda.SpringDotaPicker.controllers;

import com.jda.SpringDotaPicker.models.Hero;
import com.jda.SpringDotaPicker.services.HeroService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
