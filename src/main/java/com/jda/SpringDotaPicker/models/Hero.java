package com.jda.SpringDotaPicker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Entity
@Data
public class Hero {
    @Id
    private int id;
    private String name;
    private String imageLink;


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Hero) {
            Hero hero = (Hero) obj;
            return this.id == hero.id;
        }
        return false;
    }
}
