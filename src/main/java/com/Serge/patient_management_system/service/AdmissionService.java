package com.Serge.patient_management_system.service;

import com.Serge.patient_management_system.domain.Admission;
import com.Serge.patient_management_system.domain.BillingItem;
import com.Serge.patient_management_system.domain.Inpatient;
import com.Serge.patient_management_system.domain.Nurse;
import com.Serge.patient_management_system.enums.AdmissionStatus;
import com.Serge.patient_management_system.repository.AdmissionRepository;
import com.Serge.patient_management_system.repository.BillingItemRepository;
import com.Serge.patient_management_system.repository.InpatientRepository;
import com.Serge.patient_management_system.repository.NurseRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdmissionService {

    private final AdmissionRepository admissionRepository;
    private final InpatientRepository inpatientRepository;
    private final NurseRepository nurseRepository;
    private final BillingItemRepository billingItemRepository;

    public AdmissionService(
            AdmissionRepository admissionRepository,
            InpatientRepository inpatientRepository,
            NurseRepository nurseRepository,
            BillingItemRepository billingItemRepository) {

        this.admissionRepository = admissionRepository;
        this.inpatientRepository = inpatientRepository;
        this.nurseRepository = nurseRepository;
        this.billingItemRepository = billingItemRepository;
    }

    // SAVE A NEW ADMISSION
    public Admission saveAdmission(Admission admission) {

        // CHECK WHETHER THE ADMISSION DATE IS PROVIDED
        if (admission.getAdmissionDate() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Admission date is required");
        }

        // CHECK WHETHER THE INPATIENT IS PROVIDED
        if (admission.getInpatient() == null
                || admission.getInpatient().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Inpatient is required");
        }

        // CHECK WHETHER THE NURSE IS PROVIDED
        if (admission.getNurse() == null
                || admission.getNurse().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nurse is required");
        }

        // RETRIEVE THE EXISTING INPATIENT
        Inpatient inpatient = inpatientRepository
                .findById(admission.getInpatient().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "The inpatient with that id does not exist"));

        // RETRIEVE THE EXISTING NURSE
        Nurse nurse = nurseRepository
                .findById(admission.getNurse().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "The nurse with that id does not exist"));

        // CHECK WHETHER THE NURSE ALREADY HAS A NON-REFERRED ADMISSION
        // ON THE SAME DATE
        boolean alreadyAssigned = admissionRepository
                .existsByNurse_IdAndAdmissionDateAndStatusNot(
                        nurse.getId(),
                        admission.getAdmissionDate(),
                        AdmissionStatus.REFERRED);

        if (alreadyAssigned) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Nurse already has an admission on that date");
        }

        // ASSIGN THE EXISTING INPATIENT AND NURSE
        admission.setInpatient(inpatient);
        admission.setNurse(nurse);

        // SET THE DEFAULT ADMISSION STATUS
        admission.setStatus(AdmissionStatus.ADMITTED);

        // SAVE AND RETURN THE ADMISSION
        return admissionRepository.save(admission);
    }

    // RETRIEVE ALL ADMISSIONS
    public List<Admission> getAllAdmissions() {
        return admissionRepository.findAll();
    }

    // FIND AN ADMISSION BY ID
    public Optional<Admission> getAdmissionById(UUID id) {
        return admissionRepository.findById(id);
    }

    // FIND ADMISSIONS BY STATUS, ORDERED BY ADMISSION DATE
    public List<Admission> getAdmissionsByStatus(AdmissionStatus status) {
        return admissionRepository
                .findByStatusOrderByAdmissionDateAsc(status);
    }

    // RETRIEVE ADMISSIONS BETWEEN TWO DATES
    public List<Admission> getAdmissionsBetweenDates(
            LocalDate start, LocalDate end) {

        // VALIDATE THE DATE RANGE
        if (start == null || end == null || start.isAfter(end)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid admission date range");
        }

        return admissionRepository
                .findByAdmissionDateBetweenOrderByAdmissionDateAsc(
                        start, end);
    }

    // FIND DISTINCT INPATIENTS ASSOCIATED WITH A NURSE
    public List<Inpatient> getInpatientsByNurseId(UUID nurseId) {

        // CHECK WHETHER THE NURSE EXISTS
        if (!nurseRepository.existsById(nurseId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "The nurse with that id does not exist");
        }

        return admissionRepository
                .findDistinctInpatientsByNurseId(nurseId);
    }

    // UPDATE AN EXISTING ADMISSION WITHOUT CHANGING ITS STATUS
    public Admission updateAdmission(UUID id, Admission updatedAdmission) {

        Admission existingAdmission = admissionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "The admission with that id does not exist"));

        // CHECK WHETHER THE INPATIENT IS PROVIDED
        if (updatedAdmission.getInpatient() == null
                || updatedAdmission.getInpatient().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Inpatient is required");
        }

        // CHECK WHETHER THE NURSE IS PROVIDED
        if (updatedAdmission.getNurse() == null
                || updatedAdmission.getNurse().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nurse is required");
        }

        // RETRIEVE THE EXISTING INPATIENT
        Inpatient inpatient = inpatientRepository
                .findById(updatedAdmission.getInpatient().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "The inpatient with that id does not exist"));

        // RETRIEVE THE EXISTING NURSE
        Nurse nurse = nurseRepository
                .findById(updatedAdmission.getNurse().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "The nurse with that id does not exist"));

        // UPDATE THE ADMISSION DETAILS
        existingAdmission.setAdmissionDate(
                updatedAdmission.getAdmissionDate());

        existingAdmission.setDiagnosis(
                updatedAdmission.getDiagnosis());

        existingAdmission.setInpatient(inpatient);
        existingAdmission.setNurse(nurse);

        // SAVE WITHOUT CHANGING THE EXISTING STATUS
        return admissionRepository.save(existingAdmission);
    }

    // UPDATE AN ADMISSION STATUS ACCORDING TO THE ALLOWED TRANSITIONS
    public Admission updateAdmissionStatus(
            UUID id, AdmissionStatus newStatus) {

        Admission admission = admissionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "The admission with that id does not exist"));

        if (newStatus == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Admission status is required");
        }

        AdmissionStatus currentStatus = admission.getStatus();

        // ALLOW ONLY THE SPECIFIED STATUS TRANSITIONS
        boolean allowed =
                (currentStatus == AdmissionStatus.ADMITTED
                        && (newStatus == AdmissionStatus.UNDER_TREATMENT
                        || newStatus == AdmissionStatus.REFERRED))
                        ||
                        (currentStatus == AdmissionStatus.UNDER_TREATMENT
                                && (newStatus == AdmissionStatus.DISCHARGED
                                || newStatus == AdmissionStatus.REFERRED));

        if (!allowed) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot change status from "
                            + currentStatus + " to " + newStatus);
        }

        // UPDATE AND SAVE THE NEW STATUS
        admission.setStatus(newStatus);

        admissionRepository.save(admission);

        return admission;
    }

    // DELETE AN ADMISSION IF IT HAS NO BILLING ITEMS
    public String deleteAdmission(UUID id) {

        Admission admission = admissionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "The admission with that id does not exist"));

        // CHECK WHETHER BILLING ITEMS EXIST
        boolean hasBillingItems =
                billingItemRepository.existsByAdmission_Id(id);

        if (hasBillingItems) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "That admission has billing items, delete them first");
        }

        admissionRepository.delete(admission);

        return "Admission deleted successfully";
    }
}