package com.jda.SpringDotaPicker.repositories;

import com.jda.SpringDotaPicker.models.Hero;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HeroesRepository extends CrudRepository<Hero, Integer> {
    Hero findById(int id);

}
