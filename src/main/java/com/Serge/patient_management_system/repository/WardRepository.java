package com.Serge.patient_management_system.repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import com.Serge.patient_management_system.domain.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WardRepository extends JpaRepository<Ward, UUID> {

    // FIND A WARD BY ITS WARD NUMBER
    Optional<Ward> findByWardNumber(int wardNumber);

    // CHECK WHETHER A WARD NUMBER ALREADY EXISTS
    boolean existsByWardNumber(int wardNumber);

    // FIND WARDS THAT ARE NOT ASSIGNED TO ANY NURSE
    @Query("SELECT w FROM Ward w WHERE NOT EXISTS " +
            "(SELECT n FROM Nurse n WHERE n.ward = w) " +
            "ORDER BY w.wardNumber ASC")
    List<Ward> findFreeWards();
}

