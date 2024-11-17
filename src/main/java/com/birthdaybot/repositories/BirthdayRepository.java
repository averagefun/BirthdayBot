package com.birthdaybot.repositories;

import com.birthdaybot.model.Birthday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Repository
public interface BirthdayRepository extends JpaRepository<Birthday, Long> {

//    @Query("delete from birthday where birthday.id = ?")
//
//

    @Query("select b from Birthday b where b.owner.id = ?1")
    ArrayList<Birthday> findBirthdaysByOwnerId(Long id);

    @Query("select b from Birthday b where b.chatId = ?1")
    ArrayList<Birthday> findBirthdaysByChatId(Long id);

    @Transactional
    @Modifying
    @Query("delete from Birthday b where b.id = ?1 ")
    void deleteById(Long id);

}
