package com.jda.SpringDotaPicker.controllers;

import com.jda.SpringDotaPicker.models.Hero;
import com.jda.SpringDotaPicker.services.HeroService;
import com.jda.SpringDotaPicker.services.MatchupsUpdateService;
import com.jda.SpringDotaPicker.services.RolesUpdateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
public class HeroesController {

    private final HeroService heroService;
    private final RolesUpdateService rolesUpdateService;
    private final MatchupsUpdateService matchupsUpdateService;

    public HeroesController(HeroService heroService, RolesUpdateService rolesUpdateService, MatchupsUpdateService matchupsUpdateService) {
        this.heroService = heroService;
        this.rolesUpdateService = rolesUpdateService;
        this.matchupsUpdateService = matchupsUpdateService;
    }


    @GetMapping()
    public Iterable<Hero> index() {
        return heroService.findAll();
    }

    @GetMapping("/picker")
    public String heroPicker(Model model) {
        Iterable<Hero> heroes = heroService.findAll();
        model.addAttribute("heroes", heroes);
        return "heroes";
    }

    @GetMapping("/hero/{id}")
    public String hero(@PathVariable int id, Model model) {
        Hero hero = heroService.findById(id);
        //System.out.println(hero);
        if (hero == null) {
            System.out.println("Bad request by hero/"+id);
            return "errorpage";
        }
        model.addAttribute("hero", hero);
        return "hero";
    }

    /*
    Runs roles update by request
     */
    @GetMapping("/upd")
    public String heroUpd() {
        try {
            rolesUpdateService.updateRoles();
        } catch (IOException e) {
            System.out.println("Bad request by roles/update");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return "redirect:/heroes";
    }

    @GetMapping("/update/matchups")
    public String heroUpdMatchups() {
        matchupsUpdateService.updateMatchups();
        return "redirect:/heroes";
    }
}
