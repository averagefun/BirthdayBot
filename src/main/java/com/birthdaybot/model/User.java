package com.birthdaybot.model;


import com.birthdaybot.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users",
        indexes = {@Index(name = "i_birthday_index", columnList = "status")})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Min(-12)
    @Max(12)
    @Column(name = "timezone")
    private Integer timezone = 3;

    @Column(name = "status")
    private Status status = Status.BASE;

    @Column(name = "share_code")
    private Long shareCode;
}
