package com.birthdaybot.repositories;

import com.birthdaybot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    boolean existsById(Long aLong);
}
