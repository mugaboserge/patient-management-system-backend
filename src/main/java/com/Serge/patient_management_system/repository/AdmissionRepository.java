package com.Serge.patient_management_system.repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import com.Serge.patient_management_system.domain.Admission;
import com.Serge.patient_management_system.domain.Inpatient;
import com.Serge.patient_management_system.enums.AdmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, UUID> {

    // Find admissions by status, ordered by admission date ascending.
    List<Admission> findByStatusOrderByAdmissionDateAsc(
            AdmissionStatus status
    );

    // Find admissions between two dates, including both dates.
    List<Admission> findByAdmissionDateBetweenOrderByAdmissionDateAsc(
            LocalDate start,
            LocalDate end
    );

    // Check whether a nurse has an admission on a date
    // whose status is not the specified status.
    boolean existsByNurse_IdAndAdmissionDateAndStatusNot(
            UUID nurseId,
            LocalDate admissionDate,
            AdmissionStatus status
    );

    // Find distinct inpatients associated with a particular nurse.
    @Query("SELECT DISTINCT a.inpatient FROM Admission a " +
            "WHERE a.nurse.id = :nurseId")
    List<Inpatient> findDistinctInpatientsByNurseId(
            @Param("nurseId") UUID nurseId
    );
    // COUNT ADMISSIONS GROUPED BY STATUS
    @Query("SELECT a.status, COUNT(a) FROM Admission a GROUP BY a.status")
    List<Object[]> countAdmissionsByStatus();

    // CANCEL NON-DISCHARGED ADMISSIONS FOR A NURSE ON A SPECIFIC DATE
    @Modifying
    @Transactional
    @Query("UPDATE Admission a SET a.status = :newStatus " +
            "WHERE a.nurse.id = :nurseId " +
            "AND a.admissionDate = :date " +
            "AND a.status <> :excludedStatus")
    int cancelAdmissionsForNurseOnDate(
            @Param("nurseId") UUID nurseId,
            @Param("date") LocalDate date,
            @Param("newStatus") AdmissionStatus newStatus,
            @Param("excludedStatus") AdmissionStatus excludedStatus
    );

    // COUNT ADMISSIONS CREATED FOR A SPECIFIC DATE
    long countByAdmissionDate(LocalDate admissionDate);

    // COUNT ADMISSIONS WITH A SPECIFIC STATUS

    long countByStatus(AdmissionStatus status);
}

