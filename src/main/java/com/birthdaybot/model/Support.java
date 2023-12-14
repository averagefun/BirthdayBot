package com.birthdaybot.model;

import com.birthdaybot.model.enums.SupportStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "support")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Support {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "status", nullable = false)
    private SupportStatus status = SupportStatus.OPEN;

    @Column(name = "content", nullable = false)
    private String content;

    @Min(0)
    @Max(100)
    @Column(name = "priority", nullable = false)
    private Integer priority;
}
