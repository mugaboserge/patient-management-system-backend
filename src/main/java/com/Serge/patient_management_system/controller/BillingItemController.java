package com.Serge.patient_management_system.controller;

import com.Serge.patient_management_system.domain.BillingItem;
import com.Serge.patient_management_system.service.BillingItemService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/billing-items")
public class BillingItemController {

    private final BillingItemService billingItemService;

    public BillingItemController(BillingItemService billingItemService) {
        this.billingItemService = billingItemService;
    }

    // CREATE BILLING (KUMURWAYI USEZEREWE)

    @PostMapping("/save")
    public ResponseEntity<String> saveBillingItem(
            @RequestParam UUID admissionId,
            @RequestBody BillingItem billingItem) {

        try {
            billingItemService.saveBillingItem(admissionId, billingItem);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Billing item saved successfully");

        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getReason());
        }
    }

    // RETRIEVE ALL BILLING ITEMS

    @GetMapping("/all")
    public ResponseEntity<List<BillingItem>> getAllBillingItems() {

        return ResponseEntity.ok(
                billingItemService.getAllBillingItems());
    }

    // RETRIEVE A BILLING ITEM BY ID

    @GetMapping("/{id}")
    public ResponseEntity<?> getBillingItemById(
            @PathVariable UUID id) {

        return billingItemService.getBillingItemById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The billing item with that id does not exist"));
    }

    // RETRIEVE BILLING ITEMS BY INPATIENT FILE NUMBER

    @GetMapping("/inpatient/{fileNumber}")
    public ResponseEntity<List<BillingItem>> getBillingItemsByFileNumber(
            @PathVariable String fileNumber) {

        return ResponseEntity.ok(
                billingItemService.getBillingItemsByFileNumber(fileNumber));
    }

    // GUSIBA BILLING ITEM

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBillingItem(
            @PathVariable UUID id) {

        try {
            String result = billingItemService.deleteBillingItem(id);

            return ResponseEntity.ok(result);

        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getReason());
        }
    }
}
