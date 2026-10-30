package com.Serge.patient_management_system.service;

import com.Serge.patient_management_system.enums.AdmissionStatus;
import com.Serge.patient_management_system.repository.AdmissionRepository;
import com.Serge.patient_management_system.repository.BillingItemRepository;
import com.Serge.patient_management_system.repository.InpatientRepository;
import com.Serge.patient_management_system.repository.NurseRepository;
import com.Serge.patient_management_system.repository.WardRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final InpatientRepository inpatientRepository;
    private final NurseRepository nurseRepository;
    private final WardRepository wardRepository;
    private final AdmissionRepository admissionRepository;
    private final BillingItemRepository billingItemRepository;

    public DashboardService(
            InpatientRepository inpatientRepository,
            NurseRepository nurseRepository,
            WardRepository wardRepository,
            AdmissionRepository admissionRepository,
            BillingItemRepository billingItemRepository) {

        this.inpatientRepository = inpatientRepository;
        this.nurseRepository = nurseRepository;
        this.wardRepository = wardRepository;
        this.admissionRepository = admissionRepository;
        this.billingItemRepository = billingItemRepository;
    }

    // GET THE DASHBOARD SUMMARY

    public Map<String, Long> getDashboardSummary() {

        Map<String, Long> summary = new LinkedHashMap<>();

        summary.put("inpatients", inpatientRepository.count());
        summary.put("nurses", nurseRepository.count());
        summary.put("freeWards", wardRepository.countFreeWards());
        summary.put("admissionsToday",
                admissionRepository.countByAdmissionDate(LocalDate.now()));
        summary.put("admitted",
                admissionRepository.countByStatus(AdmissionStatus.ADMITTED));
        summary.put("billingItems", billingItemRepository.count());

        return summary;
    }
}