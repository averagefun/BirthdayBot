package com.birthdaybot.repositories;

import com.birthdaybot.model.enums.Status;
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

    @Query("select u.timezone from User u where u.id = ?1")
    Integer getTimeZone(Long id);

    @Query("select u.isGroupMode from User u where u.id = ?1")
    boolean getIsGroupMode(Long id);

    @Query("select u.groupId from User u where u.id = ?1")
    Long getGroupIdByUserId(Long id);

    @Query("select u.isGroupAdmin from User u where u.id = ?1")
    boolean getIsGroupAdmin(Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.timezone = ?1 where u.id = ?2")
    void setTimeZone(Integer timezone, Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.groupId = ?1 where u.id = ?2")
    void setGroupId(Long groupId, Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.status = ?1 where u.id = ?2")
    int updateStatusById(Status status, Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.isGroupMode = ?1 where u.id = ?2")
    int updateIsGroupModeById(Boolean isGroupMode, Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.isGroupAdmin = ?1 where u.id = ?2")
    void updateIsAdminById(boolean isAdmin, Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.lang = ?1 where u.id = ?2")
    void updateLangById(String lang, Long id);



    @Query("select u from User u where u.id = ?1")
    User getUserById(Long id);

    @Query("select u.lang from User u where u.id = ?1")
    String getLanguageCode(Long id);


}
