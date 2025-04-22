package com.jovana.notesbynona.controller;

import com.jovana.notesbynona.entity.perfume.PerfumeBrand;
import com.jovana.notesbynona.repository.perfume.PerfumeBrandRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/perfumeBrands")
@AllArgsConstructor
public class PerfumeBrandController {
    @Autowired
    private PerfumeBrandRepository brandRepository;

    @GetMapping("/getAllBrands")
    public List<PerfumeBrand> getAllBrands() {
        List<PerfumeBrand> brands = brandRepository.findAll();

        return brands;
    }
}
