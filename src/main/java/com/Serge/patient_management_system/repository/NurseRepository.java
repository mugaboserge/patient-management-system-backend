package com.Serge.patient_management_system.repository;

import com.Serge.patient_management_system.domain.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, UUID> {

    // FIND NURSES BY QUALIFICATION NAME, IGNORING CASE
    @Query("SELECT DISTINCT n FROM Nurse n " +
            "JOIN n.qualifications q " +
            "WHERE LOWER(q.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Nurse> findNursesByQualificationName(@Param("name") String name);

    // CHECK WHETHER A WARD IS ASSIGNED TO ANOTHER NURSE
    boolean existsByWard_IdAndIdNot(UUID wardId, UUID nurseId);
}