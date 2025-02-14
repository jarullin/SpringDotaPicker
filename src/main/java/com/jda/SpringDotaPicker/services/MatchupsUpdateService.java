package com.jda.SpringDotaPicker.services;

import com.jda.SpringDotaPicker.models.Hero;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class MatchupsUpdateService {
    @Scheduled(cron = "0 0 0 * * 0")
    public void updateMatchups(HeroService heroService) {

    }
}
