package com.birthdaybot.model;

import com.birthdaybot.model.enums.Permission;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="member_permissions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MemberPermission {
    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    private GroupMember groupMember;

    @Column(name = "permission")
    private Permission permission;
}
