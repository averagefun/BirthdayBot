package com.birthdaybot.repositories;

import com.birthdaybot.model.Alarm;
import com.birthdaybot.model.Birthday;
import com.birthdaybot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    @Query("select a from Alarm a where a.birthday = ?1 and a.birthday.owner = ?2")
    Alarm findByBirthdayAndBirthday_Owner(Birthday birthday, User owner);

    @Query("select (count(a) > 0) from Alarm a where a.birthday.owner = ?1")
    boolean existsByBirthday_Owner(User owner);

    @Query("select a from Alarm a where a.time <= ?1")
    List<Alarm> findByTime(Instant time);



}
