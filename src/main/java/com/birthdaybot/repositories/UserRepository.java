package com.birthdaybot.repositories;

import com.birthdaybot.model.Status;
import com.birthdaybot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    boolean existsById(Long aLong);

    @Query("select u.status from User u where u.id = ?1")
    Status getStatus(Long id);

}
