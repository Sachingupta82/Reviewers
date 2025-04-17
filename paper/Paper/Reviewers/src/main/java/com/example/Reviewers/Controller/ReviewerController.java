package com.example.Reviewers.Controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.Reviewers.Entity.ReviewerEntity;
import com.example.Reviewers.Repository.ReviewerRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Reviewer Management", description = "Operations related to reviewers") 
public class ReviewerController {
    private static final Logger logger = LoggerFactory.getLogger(ReviewerController.class);
    
    @Autowired
    private ReviewerRepository reviewerRepository;

    @GetMapping("/test")
    @Operation(summary = "Test Method", description = "Returns a greeting message.")
    public String getMethodName() {
        return "Hello Sachin";
    }

    @GetMapping("/reviewers")
    @Operation(summary = "Get All Reviewers", description = "Retrieves all reviewers data.")
    public ResponseEntity<List<ReviewerEntity>> getAllReviewers() {
        try {
            List<ReviewerEntity> reviewers = reviewerRepository.findAllByOrderByIdAsc();
            return ResponseEntity.ok(reviewers);
        } catch (Exception e) {
            logger.error("Error getting all reviewers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/reviewers/{id}")
    @Operation(summary = "Get Reviewer by ID", description = "Retrieves a reviewer by their ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "404", description = "Reviewer not found")
    })
    public ResponseEntity<ReviewerEntity> getReviewerById(@PathVariable("id") Integer id) {
        try {
            Optional<ReviewerEntity> reviewer = reviewerRepository.findById(id);
            return reviewer.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error getting reviewer with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/reviewers")
    @Operation(summary = "Create Reviewer", description = "Creates a new reviewer.")
    public ResponseEntity<?> createReviewer(@RequestBody ReviewerEntity reviewer) {
        try {
            ReviewerEntity savedReviewer = reviewerRepository.save(reviewer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReviewer);
        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation while creating reviewer", e);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email already exists"));
        } catch (Exception e) {
            logger.error("Error creating reviewer", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/reviewers/{id}")
    @Operation(summary = "Update Reviewer", description = "Updates an existing reviewer by their ID.")
    public ResponseEntity<?> updateReviewer(@PathVariable("id") int id, @RequestBody ReviewerEntity reviewer) {
        try {
            logger.info("Received update request for ID: {}", id);
            logger.debug("Request body: {}", reviewer);

            Optional<ReviewerEntity> existingReviewerOptional = reviewerRepository.findById(id);
            
            if (!existingReviewerOptional.isPresent()) {
                logger.warn("Reviewer not found with ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Reviewer with id " + id + " not found"));
            }

            ReviewerEntity existingReviewer = existingReviewerOptional.get();
            logger.debug("Current reviewer data: {}", existingReviewer);

            // Update the fields
            reviewer.setId(id);
            logger.debug("Attempting to save updated reviewer: {}", reviewer);

            try {
                ReviewerEntity savedReviewer = reviewerRepository.save(reviewer);
                logger.info("Successfully updated reviewer with ID: {}", id);
                return ResponseEntity.ok(savedReviewer);
            } catch (DataIntegrityViolationException e) {
                logger.error("Data integrity violation while updating reviewer", e);
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email already exists"));
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating reviewer with ID: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to update reviewer: " + e.getMessage()));
        }
    }

    @PatchMapping("/reviewers/{id}")
    @Operation(summary = "Partial Update Reviewer", description = "Updates specific fields of an existing reviewer.")
    public ResponseEntity<ReviewerEntity> patchReviewer(@PathVariable("id") int id, @RequestBody Map<String, Object> updates) {
        try {
            Optional<ReviewerEntity> existingReviewerOptional = reviewerRepository.findById(id);
            if (existingReviewerOptional.isPresent()) {
                ReviewerEntity existingReviewer = existingReviewerOptional.get();
                updates.forEach((key, value) -> {
                    switch (key) {
                        case "name":
                            existingReviewer.setName((String) value);
                            break;
                        case "email":
                            existingReviewer.setEmail((String) value);
                            break;
                        case "affiliation":
                            existingReviewer.setAffiliation((String) value);
                            break;
                        case "expertise":
                            existingReviewer.setExpertise((String) value);
                            break;
                    }
                });
                ReviewerEntity updatedReviewer = reviewerRepository.save(existingReviewer);
                return ResponseEntity.ok(updatedReviewer);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/reviewers/{id}")
    @Operation(summary = "Delete Reviewer by ID", description = "Deletes a reviewer by their ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reviewer successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Reviewer not found")
    })
    public ResponseEntity<?> deleteReviewerById(@PathVariable("id") int id) {
        try {
            Optional<ReviewerEntity> reviewer = reviewerRepository.findById(id);
            if (reviewer.isPresent()) {
                reviewerRepository.deleteById(id);
                return ResponseEntity.ok()
                        .body(Map.of("message", "Reviewer successfully deleted"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deleting reviewer with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error deleting reviewer: " + e.getMessage()));
        }
    }
}