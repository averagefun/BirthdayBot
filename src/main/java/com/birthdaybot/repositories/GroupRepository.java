package com.birthdaybot.repositories;

import com.birthdaybot.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("select g from Group g where g.isBotAdmin = ?1")
    Group findByIsBotAdmin(Boolean isBotAdmin);


    @Override
    Optional<Group> findById(Long aLong);
}
