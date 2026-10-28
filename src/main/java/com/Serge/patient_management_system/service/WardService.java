package com.Serge.patient_management_system.service;

import com.Serge.patient_management_system.domain.Ward;
import com.Serge.patient_management_system.repository.WardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WardService {

    private final WardRepository wardRepository;

    public WardService(WardRepository wardRepository) {
        this.wardRepository = wardRepository;

    }
    // RETRIEVE WARDS THAT ARE NOT ASSIGNED TO ANY NURSE
    public List<Ward> getFreeWards() {
        return wardRepository.findFreeWards();
    }

    // SAVE A NEW WARD
    public String saveWard(Ward ward) {
        wardRepository.save(ward);
        return "Ward saved successfully";
    }

    // RETRIEVE ALL WARDS
    public List<Ward> getAllWards() {
        return wardRepository.findAll();
    }

    // FIND A WARD BY ID
    public Optional<Ward> getWardById(UUID id) {
        return wardRepository.findById(id);
    }

    // UPDATE AN EXISTING WARD
    public String updateWard(UUID id, Ward updatedWard) {

        Optional<Ward> optionalWard = wardRepository.findById(id);

        if (optionalWard.isEmpty()) {
            return "The ward with that id does not exist";
        }

        Ward existingWard = optionalWard.get();

        existingWard.setWardNumber(updatedWard.getWardNumber());
        existingWard.setName(updatedWard.getName());

        wardRepository.save(existingWard);

        return "Ward updated successfully";
    }

    // DELETE A WARD
    public String deleteWard(UUID id) {

        if (!wardRepository.existsById(id)) {
            return "The ward with that id does not exist";
        }

        wardRepository.deleteById(id);

        return "Ward deleted successfully";
    }
}