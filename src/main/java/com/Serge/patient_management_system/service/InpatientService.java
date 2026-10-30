package com.Serge.patient_management_system.service;

import com.Serge.patient_management_system.domain.Inpatient;
import com.Serge.patient_management_system.repository.InpatientRepository;
import com.Serge.patient_management_system.repository.NurseRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InpatientService {
    private final InpatientRepository inpatientRepository;
    private final NurseRepository nurseRepository;

    public InpatientService(
            InpatientRepository inpatientRepository,
            NurseRepository nurseRepository) {

        this.inpatientRepository = inpatientRepository;
        this.nurseRepository = nurseRepository;
    }
    // GUSHAKA UMURWAYE UFITE UMU NURSEY

    public Optional<List<Inpatient>> getInpatientsByNurseId(UUID nurseId) {

        // UBANZA KUREBA KO UMU NURSEY ARIMO

        if (!nurseRepository.existsById(nurseId)) {
            return Optional.empty();
        }

        // GUHINDURA UMURWAYI UNYUZE KUMU NURSE
        return Optional.of(
                inpatientRepository.findInpatientsByNurseId(nurseId)
        );
    }
    // FOR SAVE
    public String saveInpatient(Inpatient inpatient) {
        inpatientRepository.save(inpatient);
        return "Inpatient saved successfully";
    }

    // FOR RETRIEVE  ABARWAYI BOSE

    public List<Inpatient> getAllInpatients() {
        return inpatientRepository.findAll();
    }

    // GUSHAKA UMURWAYE UKORESHEJE ID

    public Optional<Inpatient> getInpatientById(UUID id) {
        return inpatientRepository.findById(id);
    }

    // FOR UPDATE KUMURWAYI

    public String updateInpatient(UUID id, Inpatient updatedInpatient) {

        Optional<Inpatient> optionalInpatient =
                inpatientRepository.findById(id);

        if (optionalInpatient.isEmpty()) {
            return "The inpatient with that id does not exist";
        }

        Inpatient existingInpatient = optionalInpatient.get();

        existingInpatient.setFirstName(updatedInpatient.getFirstName());
        existingInpatient.setLastName(updatedInpatient.getLastName());
        existingInpatient.setFileNumber(updatedInpatient.getFileNumber());
        existingInpatient.setInsuranceNumber(
                updatedInpatient.getInsuranceNumber());
        existingInpatient.setPhone(updatedInpatient.getPhone());

        inpatientRepository.save(existingInpatient);

        return "Inpatient updated successfully";
    }

    // GUSIBA UMURWAYI BISANZWE UKORESHEJE UUID

    public String deleteInpatient(UUID id) {

        if (!inpatientRepository.existsById(id)) {
            return "The inpatient with that id does not exist";
        }

        inpatientRepository.deleteById(id);

        return "Inpatient deleted successfully";
    }

    // GUSHA UMURWAYI N.B: INGORE CASE AND SORT

    public List<Inpatient> getInpatientsByLastName(String lastName) {
        return inpatientRepository
                .findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }
}