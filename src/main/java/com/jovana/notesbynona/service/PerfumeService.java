package com.jovana.notesbynona.service;

import com.jovana.notesbynona.entity.perfume.Perfume;
import com.jovana.notesbynona.model.parfume.PerfumeCreationRequest;
import com.jovana.notesbynona.model.parfume.PerfumeRetrieveRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface PerfumeService {
    Perfume createPerfume(PerfumeCreationRequest perfumeCreationRequest);

    Perfume createPerfumeWithImage(PerfumeCreationRequest perfumeCreationRequest, MultipartFile image);

    Page<Perfume> getPerfumes(PerfumeRetrieveRequest perfumeRetrieveRequest, Pageable pageable);

    Perfume getPerfume(String perfumeId);

    void deletePerfume(Long perfumeId);

    Perfume updatePerfume(Long perfumeId, PerfumeCreationRequest perfumeCreationRequest);

    List<Perfume> getAllPerfumes();

    Optional<Perfume> findByPerfumeName(String perfumeName);

    void uploadImage(Long perfumeId, MultipartFile image);

    byte[] getPerfumeImage(Long perfumeId);

    BigDecimal updateAndRetrieveAverageRating(Long perfumeId);

}