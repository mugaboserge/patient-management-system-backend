package com.Serge.patient_management_system.service;

import com.Serge.patient_management_system.domain.Nurse;
import com.Serge.patient_management_system.repository.NurseRepository;
import org.springframework.stereotype.Service;
import com.Serge.patient_management_system.domain.Qualification;
import com.Serge.patient_management_system.domain.Ward;
import com.Serge.patient_management_system.repository.QualificationRepository;
import com.Serge.patient_management_system.repository.WardRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NurseService {

    private final NurseRepository nurseRepository;

    private final QualificationRepository qualificationRepository;
    private final WardRepository wardRepository;

    public NurseService(
            NurseRepository nurseRepository,
            QualificationRepository qualificationRepository,
            WardRepository wardRepository) {

        this.nurseRepository = nurseRepository;
        this.qualificationRepository = qualificationRepository;
        this.wardRepository = wardRepository;
    }

    // SAVE A NEW NURSE
    public String saveNurse(Nurse nurse) {
        nurseRepository.save(nurse);
        return "Nurse saved successfully";
    }

    // RETRIEVE ALL NURSES
    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }

    // FIND A NURSE BY ID
    public Optional<Nurse> getNurseById(UUID id) {
        return nurseRepository.findById(id);
    }

    // UPDATE AN EXISTING NURSE
    public String updateNurse(UUID id, Nurse updatedNurse) {

        Optional<Nurse> optionalNurse = nurseRepository.findById(id);

        if (optionalNurse.isEmpty()) {
            return "The nurse with that id does not exist";
        }

        Nurse existingNurse = optionalNurse.get();

        existingNurse.setFirstName(updatedNurse.getFirstName());
        existingNurse.setLastName(updatedNurse.getLastName());
        existingNurse.setPhone(updatedNurse.getPhone());
        existingNurse.setWard(updatedNurse.getWard());

        nurseRepository.save(existingNurse);

        return "Nurse updated successfully";
    }

    // DELETE A NURSE
    public String deleteNurse(UUID id) {

        if (!nurseRepository.existsById(id)) {
            return "The nurse with that id does not exist";
        }

        nurseRepository.deleteById(id);

        return "Nurse deleted successfully";
    }

    // FIND NURSES BY QUALIFICATION NAME
    public List<Nurse> getNursesByQualificationName(String name) {
        return nurseRepository.findNursesByQualificationName(name);
    }
    // ADD A QUALIFICATION WITHOUT REMOVING EXISTING QUALIFICATIONS
    public String addQualification(UUID nurseId, UUID qualificationId) {

        Nurse nurse = nurseRepository.findById(nurseId).orElse(null);

        if (nurse == null) {
            return "The nurse with that id does not exist";
        }

        Qualification qualification = qualificationRepository
                .findById(qualificationId)
                .orElse(null);

        if (qualification == null) {
            return "That qualification does not exist";
        }

        // CHECK WHETHER THE NURSE ALREADY HAS THIS QUALIFICATION
        if (nurse.getQualifications().stream()
                .anyMatch(q -> q.getId().equals(qualificationId))) {
            return "The nurse already has that qualification";
        }

        nurse.getQualifications().add(qualification);
        nurseRepository.save(nurse);

        return "Qualification added to the nurse";
    }
    // CHANGE A NURSE'S WARD
    public String changeWard(UUID nurseId, int wardNumber) {

        Nurse nurse = nurseRepository.findById(nurseId).orElse(null);

        if (nurse == null) {
            return "The nurse with that id does not exist";
        }

        Ward ward = wardRepository.findByWardNumber(wardNumber)
                .orElse(null);

        if (ward == null) {
            return "The ward with that wardNumber does not exist";
        }

        // CHECK WHETHER ANOTHER NURSE ALREADY OCCUPIES THE WARD
        boolean wardTaken = nurseRepository
                .existsByWard_IdAndIdNot(ward.getId(), nurseId);

        if (wardTaken) {
            return "That ward is taken by another nurse";
        }

        // ASSIGN THE NEW WARD
        nurse.setWard(ward);
        nurseRepository.save(nurse);

        return "Ward changed successfully";
    }
}