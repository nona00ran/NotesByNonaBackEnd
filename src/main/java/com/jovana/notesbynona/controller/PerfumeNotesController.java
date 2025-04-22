package com.jovana.notesbynona.controller;

import com.jovana.notesbynona.entity.perfume.PerfumeNote;
import com.jovana.notesbynona.repository.perfume.PerfumeNotesRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/perfumeNotes")
@AllArgsConstructor
public class PerfumeNotesController {
    @Autowired
    private PerfumeNotesRepository notesRepository;

    @GetMapping("/getAllNotes")
    public List<PerfumeNote> getAllNotes() {
        List<PerfumeNote> notes = notesRepository.findAll();
        return notes;
    }
}
