package com.jda.SpringDotaPicker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="heroes")
public class Hero {
    @Id
    private int id;
    private String name;
    private String imageLink;
//
//    @Transient
//    private List<String> roles;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Hero hero) {
            return this.id == hero.id;
        }
        return false;
    }
}
