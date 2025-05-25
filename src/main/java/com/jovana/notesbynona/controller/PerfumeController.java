
package com.jovana.notesbynona.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jovana.notesbynona.entity.perfume.Perfume;
import com.jovana.notesbynona.service.PerfumeService;
import com.jovana.notesbynona.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.jovana.notesbynona.model.parfume.PerfumeCreationRequest;
import com.jovana.notesbynona.model.parfume.PerfumeRetrieveRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/perfumes")
@AllArgsConstructor
public class PerfumeController {
    private final PerfumeService perfumeService;
    private final ReviewService reviewService;

    @PostMapping("/createPerfume")
    public ResponseEntity<Perfume> createPerfume(@RequestBody @Valid PerfumeCreationRequest perfumeCreationRequest) {
        Perfume perfume = perfumeService.createPerfume(perfumeCreationRequest);
        return ResponseEntity.ok(perfume);
    }

    @PostMapping("/uploadImage/{perfumeId}")
    public ResponseEntity<String> uploadImage(@PathVariable Long perfumeId,
                                              @RequestParam("image") MultipartFile image) {
        perfumeService.uploadImage(perfumeId, image);
        return ResponseEntity.ok("Image uploaded successfully");
    }

    @PostMapping(value = "/createPerfumeWithImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addPerfumeWithImage(
            @RequestPart("data") String jsonData,
            @RequestPart("image") MultipartFile image) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        PerfumeCreationRequest request = objectMapper.readValue(jsonData, PerfumeCreationRequest.class);

        Perfume createdPerfume = perfumeService.createPerfume(request);
        perfumeService.uploadImage(createdPerfume.getId(), image);

        return ResponseEntity.ok("Perfume created and image uploaded successfully.");
    }

    @GetMapping("/getPerfumes")
    public ResponseEntity<Page<Perfume>> getPerfumes(PerfumeRetrieveRequest perfumeRetrieveRequest, Pageable pageable) {
        Page<Perfume> perfume = perfumeService.getPerfumes(perfumeRetrieveRequest, pageable);
        return ResponseEntity.ok(perfume);
    }

    @GetMapping("/getPerfume/{perfumeId}")
    public ResponseEntity<Perfume> getPerfume(@PathVariable String perfumeId) {
        Perfume perfume = perfumeService.getPerfume(perfumeId);
        return ResponseEntity.ok(perfume);
    }

    @DeleteMapping("/deletePerfume/{perfumeId}")
    public ResponseEntity<Void> deletePerfume(@PathVariable Long perfumeId) {
        perfumeService.deletePerfume(perfumeId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updatePerfume/{perfumeId}")
    public ResponseEntity<Perfume> updatePerfume(@PathVariable Long perfumeId,
                                                 @RequestBody PerfumeCreationRequest perfumeCreationRequest){
        perfumeService.updatePerfume(perfumeId, perfumeCreationRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/getPerfumeImage/{perfumeId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getPerfumeImage(@PathVariable Long perfumeId) {
        byte[] image = perfumeService.getPerfumeImage(perfumeId);
        return ResponseEntity.ok(image);
    }

    @GetMapping("/averageRating")
    public BigDecimal getAverageRating(@RequestParam Long perfumeId) {
        // return reviewService.getAverageRatingForPerfume(perfumeId);
        BigDecimal averageRating = reviewService.getAverageRatingForPerfume(perfumeId);
        return averageRating;
    }
    public Double roundToTwoDecimalPlaces(Double value) {
        if (value == null) {
            return null;
        }
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
