package com.birthdaybot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "birthdays",
        indexes = {@Index(name = "i_date_index", columnList = "date")})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Birthday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "chatId", nullable = false)
    private Long chatId;

    @ManyToOne(cascade = CascadeType.ALL)
    private User owner;

    @ManyToOne(cascade = CascadeType.ALL)
    private Group group;
}
