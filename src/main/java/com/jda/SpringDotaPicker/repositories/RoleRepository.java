package com.jda.SpringDotaPicker.repositories;

import com.jda.SpringDotaPicker.models.HeroRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<HeroRole, Long> {
    public void deleteByHeroId(Integer heroId);
}
