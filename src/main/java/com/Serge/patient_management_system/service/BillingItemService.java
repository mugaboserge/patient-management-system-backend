package com.Serge.patient_management_system.service;

import com.Serge.patient_management_system.domain.Admission;
import com.Serge.patient_management_system.domain.BillingItem;
import com.Serge.patient_management_system.enums.AdmissionStatus;
import com.Serge.patient_management_system.repository.AdmissionRepository;
import com.Serge.patient_management_system.repository.BillingItemRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BillingItemService {

    private final BillingItemRepository billingItemRepository;
    private final AdmissionRepository admissionRepository;

    public BillingItemService(
            BillingItemRepository billingItemRepository,
            AdmissionRepository admissionRepository) {

        this.billingItemRepository = billingItemRepository;
        this.admissionRepository = admissionRepository;
    }

    // SAVE A BILLING ITEM FOR A DISCHARGED ADMISSION
    public BillingItem saveBillingItem(
            UUID admissionId, BillingItem billingItem) {

        // FIND THE ADMISSION OR RETURN HTTP 404
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "The admission with that id does not exist"));

        // ALLOW BILLING ITEMS ONLY FOR DISCHARGED ADMISSIONS
        if (admission.getStatus() != AdmissionStatus.DISCHARGED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Billing items are only for DISCHARGED admissions");
        }

        // VALIDATE THE BILLING ITEM AMOUNT
        if (billingItem.getAmount() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Billing item amount must be greater than zero");
        }

        // SET THE ADMISSION AND BILLING DATE
        billingItem.setAdmission(admission);
        billingItem.setBilledDate(LocalDate.now());

        // SAVE THE BILLING ITEM
        return billingItemRepository.save(billingItem);
    }

    // RETRIEVE ALL BILLING ITEMS
    public List<BillingItem> getAllBillingItems() {
        return billingItemRepository.findAll();
    }

    // FIND A BILLING ITEM BY ID
    public Optional<BillingItem> getBillingItemById(UUID id) {
        return billingItemRepository.findById(id);
    }

    // FIND BILLING ITEMS BY INPATIENT FILE NUMBER, NEWEST FIRST
    public List<BillingItem> getBillingItemsByFileNumber(
            String fileNumber) {

        return billingItemRepository
                .findByInpatientFileNumber(fileNumber);
    }

    // DELETE A BILLING ITEM
    public String deleteBillingItem(UUID id) {

        // CHECK WHETHER THE BILLING ITEM EXISTS
        if (!billingItemRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "The billing item with that id does not exist");
        }

        // DELETE THE BILLING ITEM
        billingItemRepository.deleteById(id);

        return "Billing item deleted successfully";
    }
}
