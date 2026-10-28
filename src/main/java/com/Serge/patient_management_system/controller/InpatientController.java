package com.Serge.patient_management_system.controller;

import com.Serge.patient_management_system.domain.Inpatient;
import com.Serge.patient_management_system.service.InpatientService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/inpatients")
public class InpatientController {

    private final InpatientService inpatientService;

    public InpatientController(InpatientService inpatientService) {
        this.inpatientService = inpatientService;
    }

    // CREATE A NEW INPATIENT
    @PostMapping("/save")
    public ResponseEntity<String> saveInpatient(
            @RequestBody Inpatient inpatient) {

        String result = inpatientService.saveInpatient(inpatient);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // RETRIEVE ALL INPATIENTS
    @GetMapping("/all")
    public ResponseEntity<List<Inpatient>> getAllInpatients() {

        return ResponseEntity.ok(
                inpatientService.getAllInpatients());
    }

    // RETRIEVE AN INPATIENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getInpatientById(
            @PathVariable UUID id) {

        Optional<Inpatient> inpatient =
                inpatientService.getInpatientById(id);

        if (inpatient.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The inpatient with that id does not exist");
        }

        return ResponseEntity.ok(inpatient.get());
    }

    // UPDATE AN EXISTING INPATIENT
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateInpatient(
            @PathVariable UUID id,
            @RequestBody Inpatient updatedInpatient) {

        String result = inpatientService.updateInpatient(
                id, updatedInpatient);

        if (result.equals(
                "The inpatient with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(result);
        }

        return ResponseEntity.ok(result);
    }

    // DELETE AN INPATIENT
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteInpatient(
            @PathVariable UUID id) {

        String result = inpatientService.deleteInpatient(id);

        if (result.equals(
                "The inpatient with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(result);
        }

        return ResponseEntity.ok(result);
    }

    // FIND INPATIENTS BY LAST NAME, IGNORING CASE
    @GetMapping("/by-last-name")
    public ResponseEntity<List<Inpatient>> getInpatientsByLastName(
            @RequestParam String lastName) {

        return ResponseEntity.ok(
                inpatientService.getInpatientsByLastName(lastName));
    }
}