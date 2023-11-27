package com.birthdaybot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "texts")
@IdClass(TextsId.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Texts {
    @Id
    private String tag;
    @Id
    private Long language_id;

    @Column(name = "content")
    private String content;
}

@Setter
@Getter
class TextsId implements Serializable {
    private String tag;

    public TextsId() {
    }

    public TextsId(String tag, Long language_id) {
        this.tag = tag;
        this.language_id = language_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextsId textsId = (TextsId) o;
        return Objects.equals(tag, textsId.tag) && Objects.equals(language_id, textsId.language_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, language_id);
    }

    private Long language_id;
}
