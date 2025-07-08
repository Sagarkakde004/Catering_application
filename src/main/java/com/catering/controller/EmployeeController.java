package com.catering.controller;

import com.catering.entity.Employee;
import com.catering.entity.EmployeeAssignment;
import com.catering.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Employee operations
 */
@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    /**
     * Get all employees
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
    
    /**
     * Get active employees
     */
    @GetMapping("/active")
    public ResponseEntity<List<Employee>> getActiveEmployees() {
        return ResponseEntity.ok(employeeService.getActiveEmployees());
    }
    
    /**
     * Get employee by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.findById(id);
        return employee.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create new employee
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody CreateEmployeeRequest request) {
        try {
            Employee employee = employeeService.createEmployee(
                request.getName(),
                request.getRole(),
                request.getPhoneNumber(),
                request.getAddress(),
                request.getDailyRate()
            );
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update employee
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody UpdateEmployeeRequest request) {
        try {
            Employee employee = employeeService.updateEmployee(
                id,
                request.getName(),
                request.getRole(),
                request.getPhoneNumber(),
                request.getAddress(),
                request.getDailyRate()
            );
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Delete employee
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Deactivate employee
     */
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Employee> deactivateEmployee(@PathVariable Long id) {
        try {
            Employee employee = employeeService.deactivateEmployee(id);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get employees by role
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<Employee>> getEmployeesByRole(@PathVariable String role) {
        try {
            Employee.EmployeeRole employeeRole = Employee.EmployeeRole.valueOf(role.toUpperCase());
            return ResponseEntity.ok(employeeService.getEmployeesByRole(employeeRole));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Search employees
     */
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestParam String keyword) {
        return ResponseEntity.ok(employeeService.searchEmployees(keyword));
    }
    
    /**
     * Get available employees for date
     */
    @GetMapping("/available")
    public ResponseEntity<List<Employee>> getAvailableEmployeesForDate(@RequestParam String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            return ResponseEntity.ok(employeeService.getAvailableEmployeesForDate(localDate));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get employees assigned on date
     */
    @GetMapping("/assigned")
    public ResponseEntity<List<Employee>> getEmployeesAssignedOnDate(@RequestParam String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            return ResponseEntity.ok(employeeService.getEmployeesAssignedOnDate(localDate));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Assign employee to order
     */
    @PostMapping("/{employeeId}/assign")
    public ResponseEntity<EmployeeAssignment> assignEmployeeToOrder(
            @PathVariable Long employeeId,
            @RequestBody AssignmentRequest request) {
        try {
            EmployeeAssignment assignment = employeeService.assignEmployeeToOrder(
                employeeId,
                request.getOrderId(),
                request.getAssignmentDate()
            );
            return ResponseEntity.ok(assignment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get employee assignments
     */
    @GetMapping("/{id}/assignments")
    public ResponseEntity<List<EmployeeAssignment>> getEmployeeAssignments(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeAssignments(id));
    }
    
    /**
     * Get pending payments
     */
    @GetMapping("/payments/pending")
    public ResponseEntity<List<EmployeeAssignment>> getPendingPayments() {
        return ResponseEntity.ok(employeeService.getPendingPayments());
    }
    
    /**
     * Mark payment as paid
     */
    @PutMapping("/assignments/{assignmentId}/pay")
    public ResponseEntity<EmployeeAssignment> markPaymentAsPaid(@PathVariable Long assignmentId) {
        try {
            EmployeeAssignment assignment = employeeService.markPaymentAsPaid(assignmentId);
            return ResponseEntity.ok(assignment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get employee assignment count for date
     */
    @GetMapping("/{id}/assignments/count")
    public ResponseEntity<Long> getEmployeeAssignmentCountForDate(
            @PathVariable Long id,
            @RequestParam String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            Long count = employeeService.getEmployeeAssignmentCountForDate(id, localDate);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get total pending payment for employee
     */
    @GetMapping("/{id}/payments/pending/total")
    public ResponseEntity<BigDecimal> getTotalPendingPaymentForEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getTotalPendingPaymentForEmployee(id));
    }
    
    /**
     * Get total payment for employee in date range
     */
    @GetMapping("/{id}/payments/total")
    public ResponseEntity<BigDecimal> getTotalPaymentForEmployeeInDateRange(
            @PathVariable Long id,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            BigDecimal total = employeeService.getTotalPaymentForEmployeeInDateRange(id, start, end);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get today's assignments
     */
    @GetMapping("/assignments/today")
    public ResponseEntity<List<EmployeeAssignment>> getTodaysAssignments() {
        return ResponseEntity.ok(employeeService.getTodaysAssignments());
    }
    
    // Request DTOs
    public static class CreateEmployeeRequest {
        private String name;
        private Employee.EmployeeRole role;
        private String phoneNumber;
        private String address;
        private BigDecimal dailyRate;
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Employee.EmployeeRole getRole() { return role; }
        public void setRole(Employee.EmployeeRole role) { this.role = role; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        
        public BigDecimal getDailyRate() { return dailyRate; }
        public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; }
    }
    
    public static class UpdateEmployeeRequest {
        private String name;
        private Employee.EmployeeRole role;
        private String phoneNumber;
        private String address;
        private BigDecimal dailyRate;
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Employee.EmployeeRole getRole() { return role; }
        public void setRole(Employee.EmployeeRole role) { this.role = role; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        
        public BigDecimal getDailyRate() { return dailyRate; }
        public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; }
    }
    
    public static class AssignmentRequest {
        private Long orderId;
        private LocalDate assignmentDate;
        
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        
        public LocalDate getAssignmentDate() { return assignmentDate; }
        public void setAssignmentDate(LocalDate assignmentDate) { this.assignmentDate = assignmentDate; }
    }
}

