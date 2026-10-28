package com.Serge.patient_management_system.service;

import com.Serge.patient_management_system.domain.Qualification;
import com.Serge.patient_management_system.repository.QualificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QualificationService {

    private final QualificationRepository qualificationRepository;

    public QualificationService(QualificationRepository qualificationRepository) {
        this.qualificationRepository = qualificationRepository;
    }

    // CREATE A NEW QUALIFICATION
    public String saveQualification(Qualification qualification) {
        qualificationRepository.save(qualification);
        return "Qualification saved successfully";
    }

    // RETRIEVE ALL QUALIFICATIONS
    public List<Qualification> getAllQualifications() {
        return qualificationRepository.findAll();
    }

    // FIND A QUALIFICATION BY ID
    public Optional<Qualification> getQualificationById(UUID id) {
        return qualificationRepository.findById(id);
    }

    // UPDATE AN EXISTING QUALIFICATION
    public String updateQualification(UUID id, Qualification updatedQualification) {

        Optional<Qualification> optionalQualification =
                qualificationRepository.findById(id);

        if (optionalQualification.isEmpty()) {
            return "The qualification with that id does not exist";
        }

        Qualification existingQualification = optionalQualification.get();

        existingQualification.setName(updatedQualification.getName());

        qualificationRepository.save(existingQualification);

        return "Qualification updated successfully";
    }

    // DELETE A QUALIFICATION
    public String deleteQualification(UUID id) {

        Optional<Qualification> optionalQualification =
                qualificationRepository.findById(id);

        if (optionalQualification.isEmpty()) {
            return "The qualification with that id does not exist";
        }

        Qualification qualification = optionalQualification.get();

        // PREVENT DELETING A QUALIFICATION THAT IS ASSIGNED TO NURSES
        if (qualification.getNurses() != null
                && !qualification.getNurses().isEmpty()) {
            return "Cannot delete a qualification assigned to nurses";
        }

        qualificationRepository.delete(qualification);

        return "Qualification deleted successfully";
    }

    // RETRIEVE QUALIFICATIONS THAT ARE NOT ASSIGNED TO ANY NURSE
    public List<Qualification> getUnusedQualifications() {
        return qualificationRepository.findUnusedQualifications();
    }
}