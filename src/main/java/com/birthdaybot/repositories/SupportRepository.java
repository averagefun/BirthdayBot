package com.birthdaybot.repositories;

import com.birthdaybot.model.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {
    @Query("select (count(s) > 0) from Support s where s.status = 0")
    boolean hasRequest(Integer status);

}
