package com.jda.SpringDotaPicker.repositories;

import com.jda.SpringDotaPicker.models.HeroRole;
import com.jda.SpringDotaPicker.models.HeroRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<HeroRole, HeroRoleId> {
    void deleteById_HeroId(Integer heroId);

    List<HeroRole> findById_HeroId(int heroId);
}
