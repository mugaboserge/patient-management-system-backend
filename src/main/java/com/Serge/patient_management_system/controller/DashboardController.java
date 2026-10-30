package com.Serge.patient_management_system.controller;

import com.Serge.patient_management_system.service.DashboardService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // GET THE DASHBOARD SUMMARY

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getDashboardSummary() {
        return ResponseEntity.ok(
                dashboardService.getDashboardSummary()
        );
    }
}
