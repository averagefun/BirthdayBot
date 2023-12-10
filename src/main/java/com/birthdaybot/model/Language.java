package com.birthdaybot.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="language")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Language {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "tag")
    private String tag;

    @Column(name = "nativeName", nullable = false)
    private String nativeName;

}
