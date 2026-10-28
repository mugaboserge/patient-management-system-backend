package com.Serge.patient_management_system.controller;

import com.Serge.patient_management_system.domain.Qualification;
import com.Serge.patient_management_system.service.QualificationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/qualifications")
public class QualificationController {

    private final QualificationService qualificationService;

    public QualificationController(
            QualificationService qualificationService) {
        this.qualificationService = qualificationService;
    }

    // CREATE A NEW QUALIFICATION
    @PostMapping("/save")
    public ResponseEntity<String> saveQualification(
            @RequestBody Qualification qualification) {

        String result =
                qualificationService.saveQualification(qualification);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // RETRIEVE ALL QUALIFICATIONS
    @GetMapping("/all")
    public ResponseEntity<List<Qualification>> getAllQualifications() {

        return ResponseEntity.ok(
                qualificationService.getAllQualifications());
    }

    // RETRIEVE A QUALIFICATION BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getQualificationById(
            @PathVariable UUID id) {

        Optional<Qualification> qualification =
                qualificationService.getQualificationById(id);

        if (qualification.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The qualification with that id does not exist");
        }

        return ResponseEntity.ok(qualification.get());
    }

    // UPDATE AN EXISTING QUALIFICATION
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateQualification(
            @PathVariable UUID id,
            @RequestBody Qualification updatedQualification) {

        String result = qualificationService.updateQualification(
                id, updatedQualification);

        if (result.equals(
                "The qualification with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(result);
        }

        return ResponseEntity.ok(result);
    }

    // DELETE A QUALIFICATION
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteQualification(
            @PathVariable UUID id) {

        String result = qualificationService.deleteQualification(id);

        if (result.equals(
                "The qualification with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(result);
        }

        return ResponseEntity.ok(result);
    }

    // RETRIEVE QUALIFICATIONS NOT ASSIGNED TO ANY NURSE
    @GetMapping("/unused")
    public ResponseEntity<List<Qualification>> getUnusedQualifications() {

        return ResponseEntity.ok(
                qualificationService.getUnusedQualifications());
    }
}
