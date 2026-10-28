package com.Serge.patient_management_system.controller;

import com.Serge.patient_management_system.domain.Ward;
import com.Serge.patient_management_system.service.WardService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/wards")
public class WardController {

    private final WardService wardService;

    public WardController(WardService wardService) {
        this.wardService = wardService;
    }

    // CREATE A NEW WARD
    @PostMapping("/save")
    public ResponseEntity<String> saveWard(@RequestBody Ward ward) {

        String result = wardService.saveWard(ward);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    // RETRIEVE ALL WARDS
    @GetMapping("/all")
    public ResponseEntity<List<Ward>> getAllWards() {

        List<Ward> wards = wardService.getAllWards();

        return ResponseEntity.ok(wards);
    }

    // RETRIEVE A WARD BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getWardById(@PathVariable UUID id) {

        Optional<Ward> ward = wardService.getWardById(id);

        if (ward.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("The ward with that id does not exist");
        }

        return ResponseEntity.ok(ward.get());
    }

    // UPDATE AN EXISTING WARD
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateWard(
            @PathVariable UUID id,
            @RequestBody Ward updatedWard) {

        String result = wardService.updateWard(id, updatedWard);

        if (result.equals("The ward with that id does not exist")) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(result);
        }

        return ResponseEntity.ok(result);
    }

    // DELETE A WARD
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteWard(@PathVariable UUID id) {

        String result = wardService.deleteWard(id);

        if (result.equals("The ward with that id does not exist")) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(result);
        }

        return ResponseEntity.ok(result);
    }
    // RETRIEVE ALL FREE WARDS
    @GetMapping("/free")
    public ResponseEntity<List<Ward>> getFreeWards() {
        return ResponseEntity.ok(wardService.getFreeWards());
    }
}