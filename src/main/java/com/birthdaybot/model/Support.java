package com.birthdaybot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
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

    @ManyToOne(cascade = CascadeType.ALL)
    private SupportStatus support;

    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "priority", nullable = false)
    private Integer priority;

    @PrePersist
    @PreUpdate
    private void validateTimezone() {
        if (priority < 0 || priority > 100) {
            throw new IllegalStateException("Timezone must be between -12 and 12");
        }
    }

    @Column(name = "status_id", nullable = false)
    private Integer status;

}
