package com.birthdaybot.repositories;

import com.birthdaybot.model.Language;
import com.birthdaybot.model.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    @Query("select l from Language l where l.nativeName = ?1")
    Language findByNativeName(String nativeName);

}
