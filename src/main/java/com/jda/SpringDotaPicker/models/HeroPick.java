package com.jda.SpringDotaPicker.models;

import lombok.Data;

@Data
public class HeroPick {
    private Hero hero;
    private double winProb;

    public HeroPick() {}
    public HeroPick(Hero hero, double winProb) {
        this.hero = hero;
        this.winProb = winProb;
    }
}
