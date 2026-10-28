package com.Serge.patient_management_system.repository;

import com.Serge.patient_management_system.domain.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QualificationRepository
        extends JpaRepository<Qualification, UUID> {

    // FIND QUALIFICATIONS THAT ARE NOT ASSIGNED TO ANY NURSE
    @Query("SELECT q FROM Qualification q WHERE q.nurses IS EMPTY ORDER BY q.name ASC")
    List<Qualification> findUnusedQualifications();
}