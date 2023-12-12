package com.birthdaybot.repositories;

import com.birthdaybot.model.Texts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TextRepository extends JpaRepository<Texts, Long> {
    @Query("select t from Texts t where t.tag = ?1 and t.language_id = ?2")
    Texts findByTagAndLanguage_id(String tag, Long language_id);

}
