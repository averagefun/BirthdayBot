package com.birthdaybot.repositories;

import com.birthdaybot.model.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    @Query("select g from GroupMember g where g.user.id = ?1 and g.group.id = ?2")
    GroupMember findByUser_IdAndGroup_Id(Long id, Long id1);

    @Query("select count(g) from GroupMember g where g.user.id = ?1")
    long countByUser_Id(Long id);


}
