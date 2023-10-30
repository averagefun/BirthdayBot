package com.birthdaybot.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "timezone")
    private Integer timezone=3;

    @Column(name = "status")
    private Status status=Status.BASE;

    @Column(name = "lang")
    private String lang;

    @Column(name = "shareCode")
    private Long shareCode;

    public void generateShareCode(){
       shareCode=id+(long)(Math.PI*100000000);
    }
}
