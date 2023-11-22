package com.birthdaybot.model;

import com.birthdaybot.model.enums.Permission;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="memberPermission")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MemberPermission {
    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    private Group group;

    @Column(name = "permission")
    private Permission permission;
}
