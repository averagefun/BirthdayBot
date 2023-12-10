package com.birthdaybot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="groups")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Group
{
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "timeZone")
    private Integer timeZone;
    @Column(name = "isBotAdmin")
    private Boolean isBotAdmin;

}
