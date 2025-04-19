package com.jovana.notesbynona.repository.perfume;

import com.jovana.notesbynona.entity.perfume.PerfumeBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfumeBrandRepository extends JpaRepository<PerfumeBrand, Long> {
    Optional<PerfumeBrand> findByBrandName(String brandName);
}
