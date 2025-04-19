package com.jovana.notesbynona.entity.perfume;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "perfume_notes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PerfumeNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private Long id;

    @Column(name = "note_name",nullable = false)
    @JsonProperty("note_name")
    private String noteName;

    public PerfumeNote(String noteName) {
        this.noteName = noteName;
    }
}
