package com.jda.SpringDotaPicker.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class HeroRoleId implements Serializable {
    private int heroId;
    private String role;
}
