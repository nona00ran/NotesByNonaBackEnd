package com.jovana.notesbynona.repository.perfume;

import com.jovana.notesbynona.entity.perfume.Perfume;
import com.jovana.notesbynona.entity.perfume.PerfumeBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PerfumeBrandRepository extends JpaRepository<PerfumeBrand, Long>, JpaSpecificationExecutor<PerfumeBrand> {
    Optional<PerfumeBrand> findByBrandName(String brandName);
}
