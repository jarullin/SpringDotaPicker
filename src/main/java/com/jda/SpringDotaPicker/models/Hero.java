package com.jda.SpringDotaPicker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Hero {
    @Id
    private int id;
    private String name;
    private String imageLink;


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Hero hero) {
            return this.id == hero.id;
        }
        return false;
    }
}
