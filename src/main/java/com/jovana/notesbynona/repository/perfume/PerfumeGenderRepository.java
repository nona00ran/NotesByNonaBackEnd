package com.jovana.notesbynona.repository.perfume;

import com.jovana.notesbynona.entity.perfume.PerfumeGender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PerfumeGenderRepository extends JpaRepository<PerfumeGender, Long>, JpaSpecificationExecutor<PerfumeGender> {
    Optional<PerfumeGender> findByGenderName(String genderName);
}
