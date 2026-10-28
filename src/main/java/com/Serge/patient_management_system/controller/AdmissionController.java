package com.Serge.patient_management_system.controller;

import com.Serge.patient_management_system.domain.Admission;
import com.Serge.patient_management_system.domain.Inpatient;
import com.Serge.patient_management_system.enums.AdmissionStatus;
import com.Serge.patient_management_system.service.AdmissionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/admissions")
public class AdmissionController {

    private final AdmissionService admissionService;

    public AdmissionController(AdmissionService admissionService) {
        this.admissionService = admissionService;
    }

    // CREATE A NEW ADMISSION
    @PostMapping("/save")
    public ResponseEntity<?> saveAdmission(
            @RequestBody Admission admission) {

        try {
            admissionService.saveAdmission(admission);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Admission saved successfully");

        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getReason());
        }
    }

    // RETRIEVE ALL ADMISSIONS
    @GetMapping("/all")
    public ResponseEntity<List<Admission>> getAllAdmissions() {

        return ResponseEntity.ok(
                admissionService.getAllAdmissions());
    }

    // RETRIEVE AN ADMISSION BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdmissionById(
            @PathVariable UUID id) {

        Optional<Admission> admission =
                admissionService.getAdmissionById(id);

        if (admission.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The admission with that id does not exist");
        }

        return ResponseEntity.ok(admission.get());
    }

    // UPDATE AN EXISTING ADMISSION WITHOUT CHANGING ITS STATUS
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAdmission(
            @PathVariable UUID id,
            @RequestBody Admission updatedAdmission) {

        try {
            Admission result = admissionService.updateAdmission(
                    id, updatedAdmission);

            return ResponseEntity.ok(result);

        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getReason());
        }
    }

    // DELETE AN ADMISSION
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAdmission(
            @PathVariable UUID id) {

        try {
            String result = admissionService.deleteAdmission(id);

            return ResponseEntity.ok(result);

        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getReason());
        }
    }

    // FIND ADMISSIONS BY STATUS
    @GetMapping("/by-status")
    public ResponseEntity<?> getAdmissionsByStatus(
            @RequestParam String status) {

        try {
            AdmissionStatus admissionStatus =
                    AdmissionStatus.valueOf(status.toUpperCase());

            return ResponseEntity.ok(
                    admissionService.getAdmissionsByStatus(
                            admissionStatus));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body("Invalid admission status");
        }
    }

    // FIND ADMISSIONS BETWEEN TWO DATES
    @GetMapping("/between")
    public ResponseEntity<?> getAdmissionsBetweenDates(
            @RequestParam String start,
            @RequestParam String end) {

        try {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);

            return ResponseEntity.ok(
                    admissionService.getAdmissionsBetweenDates(
                            startDate, endDate));

        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest()
                    .body("Invalid date format. Use YYYY-MM-DD");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getReason());
        }
    }

    // FIND INPATIENTS ASSOCIATED WITH A PARTICULAR NURSE
    @GetMapping("/inpatients/of-nurse/{nurseId}")
    public ResponseEntity<?> getInpatientsByNurse(
            @PathVariable UUID nurseId) {

        try {
            List<Inpatient> inpatients =
                    admissionService.getInpatientsByNurseId(nurseId);

            return ResponseEntity.ok(inpatients);

        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getReason());
        }
    }

    // UPDATE AN ADMISSION STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateAdmissionStatus(
            @PathVariable UUID id,
            @RequestParam String status) {

        AdmissionStatus newStatus;

        // VALIDATE THE REQUESTED STATUS
        try {
            newStatus = AdmissionStatus.valueOf(
                    status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body("Invalid admission status");
        }

        try {
            admissionService.updateAdmissionStatus(id, newStatus);

            return ResponseEntity.ok(
                    "Admission status updated successfully");

        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getReason());
        }
    }
}
