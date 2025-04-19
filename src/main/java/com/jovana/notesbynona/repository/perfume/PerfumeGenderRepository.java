package com.jovana.notesbynona.repository.perfume;

import com.jovana.notesbynona.entity.perfume.PerfumeGender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfumeGenderRepository extends JpaRepository<PerfumeGender, Long> {
    Optional<PerfumeGender> findByGenderName(String genderName);
}
