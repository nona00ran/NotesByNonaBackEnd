package com.jovana.notesbynona.repository.perfume;

import com.jovana.notesbynona.entity.perfume.Perfume;
import com.jovana.notesbynona.entity.perfume.PerfumeNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PerfumeNotesRepository extends JpaRepository<PerfumeNote, Long>, JpaSpecificationExecutor<PerfumeNote> {
    Optional<PerfumeNote> findPerfumeNoteByNoteName(String noteName);
}