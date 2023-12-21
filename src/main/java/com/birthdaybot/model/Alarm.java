package com.birthdaybot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;


@Getter
@Setter
@Entity
@Table(name="alarms")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_alarms_seq")
    @SequenceGenerator(
            name = "custom_alarm_seq",
            allocationSize = 1
    )
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "time", nullable = false)
    private Instant time;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "birthday_id", nullable = false)
    private Birthday birthday;
}
