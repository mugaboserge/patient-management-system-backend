package com.Serge.patient_management_system.repository;

import com.Serge.patient_management_system.domain.BillingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BillingItemRepository extends JpaRepository<BillingItem, UUID> {

    // FIND BILLING ITEMS BY INPATIENT FILE NUMBER, NEWEST FIRST
    @Query("SELECT b FROM BillingItem b " +
            "WHERE b.admission.inpatient.fileNumber = :fileNumber " +
            "ORDER BY b.billedDate DESC")
    List<BillingItem> findByInpatientFileNumber(
            @Param("fileNumber") String fileNumber
    );

    // CHECK WHETHER AN ADMISSION HAS BILLING ITEMS
    boolean existsByAdmission_Id(UUID admissionId);
}
