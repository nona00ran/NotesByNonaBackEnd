package com.jovana.notesbynona.service.impl;

import com.jovana.notesbynona.entity.perfume.Perfume;
import com.jovana.notesbynona.entity.perfume.PerfumeBrand;
import com.jovana.notesbynona.entity.perfume.PerfumeGender;
import com.jovana.notesbynona.entity.perfume.PerfumeNote;
import com.jovana.notesbynona.exceptions.DataNotFoundError;
import com.jovana.notesbynona.exceptions.PerfumeAlreadyExistsException;
import com.jovana.notesbynona.model.enums.EnumUtils;
import com.jovana.notesbynona.model.enums.SortBy;
import com.jovana.notesbynona.model.enums.SortOrder;
import com.jovana.notesbynona.model.parfume.PerfumeCreationRequest;
import com.jovana.notesbynona.model.parfume.PerfumeRetrieveRequest;
import com.jovana.notesbynona.repository.perfume.PerfumeBrandRepository;
import com.jovana.notesbynona.repository.perfume.PerfumeGenderRepository;
import com.jovana.notesbynona.repository.perfume.PerfumeNotesRepository;
import com.jovana.notesbynona.repository.perfume.PerfumeRepository;
import com.jovana.notesbynona.service.PerfumeService;
import com.jovana.notesbynona.validation.PerfumeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PerfumeServiceImpl implements PerfumeService {
    private final PerfumeRepository perfumeRepository;
    private final PerfumeGenderRepository perfumeGenderRepository;
    private final PerfumeNotesRepository perfumeNotesRepository;
    private final PerfumeBrandRepository perfumeBrandRepository;
    @Value("${image.upload.dir}")
    private String uploadDir;
    @Override
    public Perfume createPerfume(PerfumeCreationRequest perfumeCreationRequest) {
        Perfume perfume = getOrCreatePerfumeEntities(perfumeCreationRequest);
        return perfumeRepository.save(perfume);
    }

    @Override
    public Page<Perfume> getPerfumes(PerfumeRetrieveRequest perfumeRetrieveRequest, Pageable pageable) {
        if(perfumeRetrieveRequest.getSortOrder() != null && perfumeRetrieveRequest.getSortBy() == null) {
            throw new IllegalArgumentException("Sort by must be specified if sort order is specified.");
        } else if (perfumeRetrieveRequest.getSortBy()!=null &&!EnumUtils.isInEnum(perfumeRetrieveRequest.getSortBy(), SortBy.class)){
            throw new IllegalArgumentException("Invalid sort by: " + perfumeRetrieveRequest.getSortBy());
        }

        Specification<Perfume> spec = Specification.where(null);

        if (perfumeRetrieveRequest.getGenderName() != null) {
            spec = spec.and(PerfumeSpecification.hasGender(perfumeRetrieveRequest.getGenderName()));
        }
        if (perfumeRetrieveRequest.getMinPrice() != null) {
            spec = spec.and(PerfumeSpecification.hasMinPrice(perfumeRetrieveRequest.getMinPrice()));
        }
        if (perfumeRetrieveRequest.getMaxPrice() != null) {
            spec = spec.and(PerfumeSpecification.hasMaxPrice(perfumeRetrieveRequest.getMaxPrice()));
        }

        if(perfumeRetrieveRequest.getSortBy() != null){
            boolean ascending = perfumeRetrieveRequest.getSortOrder() == null || perfumeRetrieveRequest.getSortOrder().equalsIgnoreCase(SortOrder.ASC.name());
            spec = spec.and(PerfumeSpecification.sortBy(perfumeRetrieveRequest.getSortBy(), ascending));
        }

        return perfumeRepository.findAll(spec, pageable);
    }
    @Override
    public Perfume getPerfume(String perfumeId) {
        return perfumeRepository.findById(Long.valueOf(perfumeId)).orElseThrow(() -> new PerfumeAlreadyExistsException("Perfume not found"));
    }

    @Override
    public void deletePerfume(Long perfumeId) {
        if (!perfumeRepository.existsById(perfumeId)) {
            throw new DataNotFoundError("Perfume not found with ID: " + perfumeId);
        }
        perfumeRepository.deleteById(perfumeId);
    }

    @Override
    public Perfume updatePerfume(Long perfumeId, PerfumeCreationRequest perfumeCreationRequest) {
        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new DataNotFoundError("Perfume not found with ID: " + perfumeId));

        PerfumeGender perfumeGender = perfumeGenderRepository.findByGenderName(perfumeCreationRequest.getGenderName().toUpperCase())
                .orElseThrow(() -> new DataNotFoundError("Perfume gender not found with name: " + perfumeCreationRequest.getGenderName()));

        PerfumeBrand brand = perfumeBrandRepository.findByBrandName(perfumeCreationRequest.getBrandName().toUpperCase())
                .orElseGet(() -> perfumeBrandRepository.save(new PerfumeBrand(perfumeCreationRequest.getBrandName().toUpperCase())));
        perfume.setPerfumeName(perfumeCreationRequest.getPerfumeName());
        perfume.setPrice(perfumeCreationRequest.getPrice());
        perfume.setPerfumeGender(perfumeGender);
        perfume.setPerfumeBrand(brand);
        perfume.setBaseNotes(getOrCreatePerfumeNotes(perfumeCreationRequest.getBaseNotes()));
        perfume.setMiddleNotes(getOrCreatePerfumeNotes(perfumeCreationRequest.getMiddleNotes()));
        perfume.setTopNotes(getOrCreatePerfumeNotes(perfumeCreationRequest.getTopNotes()));

        return perfumeRepository.save(perfume);
    }

    @Override
    public List<Perfume> getAllPerfumes() {
        return List.of();
    }


    @Override
    public Optional<Perfume> findByPerfumeName(String perfumeName) {
        return perfumeRepository.findByPerfumeName(perfumeName);
    }

    private Perfume getOrCreatePerfumeEntities(PerfumeCreationRequest perfumeCreationRequest) {
        PerfumeGender gender = perfumeGenderRepository.findByGenderName(perfumeCreationRequest.getGenderName().toUpperCase())
                .orElseGet(() -> perfumeGenderRepository.save(new PerfumeGender(perfumeCreationRequest.getGenderName().toUpperCase())));
        PerfumeBrand brand = perfumeBrandRepository.findByBrandName(perfumeCreationRequest.getBrandName().toUpperCase())
                .orElseGet(() -> perfumeBrandRepository.save(new PerfumeBrand(perfumeCreationRequest.getBrandName().toUpperCase())));
        perfumeRepository.findByPerfumeName(perfumeCreationRequest.getPerfumeName()).ifPresent(user -> {
            throw new PerfumeAlreadyExistsException("Perfume already exists");
        });
        Perfume perfume = new Perfume();
        perfume.setPerfumeGender(gender);
        perfume.setPerfumeBrand(brand);
        perfume.setPerfumeName(perfumeCreationRequest.getPerfumeName());
        perfume.setPrice(perfumeCreationRequest.getPrice());
        perfume.setBaseNotes(getOrCreatePerfumeNotes(perfumeCreationRequest.getBaseNotes()));
        perfume.setMiddleNotes(getOrCreatePerfumeNotes(perfumeCreationRequest.getMiddleNotes()));
        perfume.setTopNotes(getOrCreatePerfumeNotes(perfumeCreationRequest.getTopNotes()));
        return perfume;
    }

    private Set<PerfumeNote> getOrCreatePerfumeNotes(Set<PerfumeNote> notes) {
        Set<PerfumeNote> perfumeNotes = new HashSet<>();
        notes.forEach(note -> {
            PerfumeNote perfumeNote = perfumeNotesRepository.findPerfumeNoteByNoteName(note.getNoteName().toUpperCase())
                    .orElseGet(() -> perfumeNotesRepository.save(new PerfumeNote(note.getNoteName().toUpperCase())));
            perfumeNotes.add(perfumeNote);
        });
        return perfumeNotes;
    }

    @Override
    public void uploadImage(Long perfumeId, MultipartFile image) {
        perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new DataNotFoundError("Perfume not found with ID: " + perfumeId));
        try{
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // Save the file
            image.transferTo(new File(uploadDir + perfumeId + ".jpg"));
        }catch (IOException e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }


}
