package com.Serge.patient_management_system.repository;

import com.Serge.patient_management_system.domain.Inpatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InpatientRepository extends JpaRepository<Inpatient, UUID> {

    // Find inpatients by last name, ignoring case,
    // and sort by first name in ascending order.
    List<Inpatient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

}