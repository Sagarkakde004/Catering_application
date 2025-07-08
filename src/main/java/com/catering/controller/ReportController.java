package com.catering.controller;

import com.catering.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * REST Controller for Reports and Dashboard operations
 */
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    /**
     * Get dashboard summary
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        return ResponseEntity.ok(reportService.getDashboardSummary());
    }
    
    /**
     * Get monthly report
     */
    @GetMapping("/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyReport(
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(reportService.getMonthlyReport(month, year));
    }
    
    /**
     * Get employee performance report
     */
    @GetMapping("/employee-performance")
    public ResponseEntity<Map<String, Object>> getEmployeePerformanceReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return ResponseEntity.ok(reportService.getEmployeePerformanceReport(start, end));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get resource utilization report
     */
    @GetMapping("/resource-utilization")
    public ResponseEntity<Map<String, Object>> getResourceUtilizationReport() {
        return ResponseEntity.ok(reportService.getResourceUtilizationReport());
    }
    
    /**
     * Get financial report
     */
    @GetMapping("/financial")
    public ResponseEntity<Map<String, Object>> getFinancialReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return ResponseEntity.ok(reportService.getFinancialReport(start, end));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get task management report
     */
    @GetMapping("/task-management")
    public ResponseEntity<Map<String, Object>> getTaskManagementReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return ResponseEntity.ok(reportService.getTaskManagementReport(start, end));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get customer analysis report
     */
    @GetMapping("/customer-analysis")
    public ResponseEntity<Map<String, Object>> getCustomerAnalysisReport() {
        return ResponseEntity.ok(reportService.getCustomerAnalysisReport());
    }
}

