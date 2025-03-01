package com.jda.SpringDotaPicker.repositories;

import com.jda.SpringDotaPicker.models.HeroRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<HeroRole, Long> {
    void deleteById_HeroId(Integer heroId);

    List<HeroRole> findById_HeroId(int heroId);
}
