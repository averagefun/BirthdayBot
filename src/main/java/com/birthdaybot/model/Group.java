package com.birthdaybot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Min(-12)
    @Max(12)
    @Column(name = "timezone")
    private Integer timeZone;


    @Column(name = "is_bot_admin")
    private Boolean isBotAdmin;

}
