package com.birthdaybot.repositories;

import com.birthdaybot.model.Status;
import com.birthdaybot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    boolean existsById(Long aLong);

    @Override
    void deleteById(Long aLong);

    @Query("select u.status from User u where u.id = ?1")
    Status getStatus(Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.status = ?1 where u.id = ?2")
    int updateStatusById(Status status, Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.lang = ?1 where u.id = ?2")
    void updateLangById(String lang, Long id);



    @Query("select u from User u where u.id = ?1")
    User getUserById(Long id);

    @Query("select u.lang from User u where u.id = ?1")
    String getLanguageCode(Long id);







}
