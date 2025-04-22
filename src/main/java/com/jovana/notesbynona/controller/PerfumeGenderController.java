package com.jovana.notesbynona.controller;

import com.jovana.notesbynona.entity.perfume.PerfumeGender;
import com.jovana.notesbynona.repository.perfume.PerfumeGenderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/perfumeGenders")
@AllArgsConstructor
public class PerfumeGenderController {
    @Autowired
    private PerfumeGenderRepository genderRepository;


    @GetMapping("/getAllGenders")
    public List<PerfumeGender> getAllGenders() {
        List<PerfumeGender> genders = genderRepository.findAll();

        return genders;
    }
}
