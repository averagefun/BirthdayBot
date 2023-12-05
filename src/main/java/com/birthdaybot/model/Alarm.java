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
@Table(name="alarm")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_alarm_seq")
    @SequenceGenerator(
            name = "custom_alarm_seq",
            allocationSize = 1
    )
    @Column(name = "id", nullable = false)
    private Long id;

    @Min(0)
    @Max(1440)
    @Column(name = "time", nullable = false)
    private Integer time;

    @ManyToOne(cascade = CascadeType.ALL)
    private Birthday birthday;
}
