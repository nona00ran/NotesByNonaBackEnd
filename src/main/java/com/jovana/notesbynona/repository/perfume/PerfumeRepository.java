package com.jovana.notesbynona.repository.perfume;

import com.jovana.notesbynona.entity.perfume.Perfume;
import com.jovana.notesbynona.entity.perfume.PerfumeGender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PerfumeRepository extends JpaRepository<Perfume, Long>, JpaSpecificationExecutor<Perfume> {

    Optional<Perfume> findByPerfumeName(String perfumeName);

    Page<Perfume> findAll(Pageable pageable);

}
