package com.Serge.patient_management_system.controller;

import com.Serge.patient_management_system.domain.Nurse;
import com.Serge.patient_management_system.service.NurseService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/nurses")
public class NurseController {

    private final NurseService nurseService;

    public NurseController(NurseService nurseService) {
        this.nurseService = nurseService;
    }

    // CREATE A NEW NURSE
    @PostMapping("/save")
    public ResponseEntity<String> saveNurse(@RequestBody Nurse nurse) {

        String result = nurseService.saveNurse(nurse);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // RETRIEVE ALL NURSES
    @GetMapping("/all")
    public ResponseEntity<List<Nurse>> getAllNurses() {

        return ResponseEntity.ok(nurseService.getAllNurses());
    }

    // RETRIEVE A NURSE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getNurseById(@PathVariable UUID id) {

        return nurseService.getNurseById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The nurse with that id does not exist"));
    }

    // UPDATE AN EXISTING NURSE
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateNurse(
            @PathVariable UUID id,
            @RequestBody Nurse updatedNurse) {

        String result = nurseService.updateNurse(id, updatedNurse);

        if (result.equals("The nurse with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.ok(result);
    }

    // DELETE A NURSE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteNurse(@PathVariable UUID id) {

        String result = nurseService.deleteNurse(id);

        if (result.equals("The nurse with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.ok(result);
    }

    // FIND NURSES BY QUALIFICATION NAME
    @GetMapping("/by-qualification")
    public ResponseEntity<List<Nurse>> getNursesByQualification(
            @RequestParam String name) {

        return ResponseEntity.ok(
                nurseService.getNursesByQualificationName(name));
    }

    // ADD A QUALIFICATION TO AN EXISTING NURSE
    @PutMapping("/{nurseId}/qualifications/{qualificationId}")
    public ResponseEntity<String> addQualification(
            @PathVariable UUID nurseId,
            @PathVariable UUID qualificationId) {

        String result = nurseService.addQualification(
                nurseId, qualificationId);

        if (result.equals("The nurse with that id does not exist")
                || result.equals("That qualification does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        if (result.equals("The nurse already has that qualification")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }

        return ResponseEntity.ok(result);
    }

    // CHANGE A NURSE'S WARD USING ITS WARD NUMBER
    @PatchMapping("/{nurseId}/ward")
    public ResponseEntity<String> changeWard(
            @PathVariable UUID nurseId,
            @RequestParam int wardNumber) {

        String result = nurseService.changeWard(nurseId, wardNumber);

        if (result.equals("The nurse with that id does not exist")
                || result.equals("The ward with that wardNumber does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        if (result.equals("That ward is taken by another nurse")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }

        return ResponseEntity.ok(result);
    }
}