package com.jovana.notesbynona.repository.perfume;

import com.jovana.notesbynona.entity.perfume.PerfumeNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfumeNotesRepository extends JpaRepository<PerfumeNote, Long> {
    Optional<PerfumeNote> findPerfumeNoteByNoteName(String noteName);
}