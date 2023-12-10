package com.birthdaybot.repositories;

import com.birthdaybot.model.SupportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupportStatusRepository extends JpaRepository<SupportStatus, Long> {


    @Override
    Optional<SupportStatus> findById(Long aLong);
}
