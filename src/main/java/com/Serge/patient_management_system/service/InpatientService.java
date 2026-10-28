package com.Serge.patient_management_system.service;

import com.Serge.patient_management_system.domain.Inpatient;
import com.Serge.patient_management_system.repository.InpatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InpatientService {

    private final InpatientRepository inpatientRepository;

    public InpatientService(InpatientRepository inpatientRepository) {
        this.inpatientRepository = inpatientRepository;
    }

    // SAVE A NEW INPATIENT
    public String saveInpatient(Inpatient inpatient) {
        inpatientRepository.save(inpatient);
        return "Inpatient saved successfully";
    }

    // RETRIEVE ALL INPATIENTS
    public List<Inpatient> getAllInpatients() {
        return inpatientRepository.findAll();
    }

    // FIND AN INPATIENT BY ID
    public Optional<Inpatient> getInpatientById(UUID id) {
        return inpatientRepository.findById(id);
    }

    // UPDATE AN EXISTING INPATIENT
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

    // DELETE AN INPATIENT
    public String deleteInpatient(UUID id) {

        if (!inpatientRepository.existsById(id)) {
            return "The inpatient with that id does not exist";
        }

        inpatientRepository.deleteById(id);

        return "Inpatient deleted successfully";
    }

    // FIND INPATIENTS BY LAST NAME, IGNORING CASE AND SORTING BY FIRST NAME
    public List<Inpatient> getInpatientsByLastName(String lastName) {
        return inpatientRepository
                .findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }
}