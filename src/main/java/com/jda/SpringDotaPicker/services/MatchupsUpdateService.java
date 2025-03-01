package com.jda.SpringDotaPicker.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MatchupsUpdateService {
    private HeroService heroService;
    // TODO: implement
    // Should get matchup stats from opendotaAPI
    @Scheduled(cron = "0 0 0 * * 0")
    public void updateMatchups() {

    }
}
