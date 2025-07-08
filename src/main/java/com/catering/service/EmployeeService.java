package com.catering.service;

import com.catering.entity.Employee;
import com.catering.entity.EmployeeAssignment;
import com.catering.entity.Order;
import com.catering.repository.EmployeeRepository;
import com.catering.repository.EmployeeAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Employee operations
 */
@Service
@Transactional
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EmployeeAssignmentRepository employeeAssignmentRepository;
    
    @Autowired
    private OrderService orderService;
    
    /**
     * Save or update employee
     */
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    
    /**
     * Find employee by ID
     */
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }
    
    /**
     * Get all employees
     */
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    /**
     * Get active employees
     */
    public List<Employee> getActiveEmployees() {
        return employeeRepository.findByIsActiveTrue();
    }
    
    /**
     * Delete employee
     */
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
    
    /**
     * Deactivate employee
     */
    public Employee deactivateEmployee(Long id) {
        Optional<Employee> employeeOpt = findById(id);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            employee.setIsActive(false);
            return saveEmployee(employee);
        }
        throw new RuntimeException("Employee not found with ID: " + id);
    }
    
    /**
     * Get employees by role
     */
    public List<Employee> getEmployeesByRole(Employee.EmployeeRole role) {
        return employeeRepository.findByRoleAndIsActiveTrue(role);
    }
    
    /**
     * Search employees
     */
    public List<Employee> searchEmployees(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getActiveEmployees();
        }
        return employeeRepository.searchActiveEmployees(keyword.trim());
    }
    
    /**
     * Get available employees for a date
     */
    public List<Employee> getAvailableEmployeesForDate(LocalDate date) {
        return employeeRepository.findAvailableEmployeesForDate(date);
    }
    
    /**
     * Get employees assigned on a date
     */
    public List<Employee> getEmployeesAssignedOnDate(LocalDate date) {
        return employeeRepository.findEmployeesAssignedOnDate(date);
    }
    
    /**
     * Create new employee
     */
    public Employee createEmployee(String name, Employee.EmployeeRole role, String phoneNumber, 
                                 String address, BigDecimal dailyRate) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setRole(role);
        employee.setPhoneNumber(phoneNumber);
        employee.setAddress(address);
        employee.setDailyRate(dailyRate);
        employee.setIsActive(true);
        
        return saveEmployee(employee);
    }
    
    /**
     * Update employee
     */
    public Employee updateEmployee(Long id, String name, Employee.EmployeeRole role, 
                                 String phoneNumber, String address, BigDecimal dailyRate) {
        Optional<Employee> employeeOpt = findById(id);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            employee.setName(name);
            employee.setRole(role);
            employee.setPhoneNumber(phoneNumber);
            employee.setAddress(address);
            employee.setDailyRate(dailyRate);
            
            return saveEmployee(employee);
        }
        throw new RuntimeException("Employee not found with ID: " + id);
    }
    
    /**
     * Assign employee to order
     */
    public EmployeeAssignment assignEmployeeToOrder(Long employeeId, Long orderId, LocalDate assignmentDate) {
        Optional<Employee> employeeOpt = findById(employeeId);
        Optional<Order> orderOpt = orderService.findById(orderId);
        
        if (!employeeOpt.isPresent()) {
            throw new RuntimeException("Employee not found with ID: " + employeeId);
        }
        if (!orderOpt.isPresent()) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
        
        EmployeeAssignment assignment = new EmployeeAssignment();
        assignment.setEmployee(employeeOpt.get());
        assignment.setOrder(orderOpt.get());
        assignment.setAssignmentDate(assignmentDate);
        assignment.setPaymentAmount(employeeOpt.get().getDailyRate());
        assignment.setPaymentStatus(EmployeeAssignment.PaymentStatus.PENDING);
        
        return employeeAssignmentRepository.save(assignment);
    }
    
    /**
     * Get employee assignments
     */
    public List<EmployeeAssignment> getEmployeeAssignments(Long employeeId) {
        return employeeAssignmentRepository.findByEmployeeId(employeeId);
    }
    
    /**
     * Get order assignments
     */
    public List<EmployeeAssignment> getOrderAssignments(Long orderId) {
        return employeeAssignmentRepository.findByOrderId(orderId);
    }
    
    /**
     * Get pending payments
     */
    public List<EmployeeAssignment> getPendingPayments() {
        return employeeAssignmentRepository.findPendingPayments();
    }
    
    /**
     * Mark payment as paid
     */
    public EmployeeAssignment markPaymentAsPaid(Long assignmentId) {
        Optional<EmployeeAssignment> assignmentOpt = employeeAssignmentRepository.findById(assignmentId);
        if (assignmentOpt.isPresent()) {
            EmployeeAssignment assignment = assignmentOpt.get();
            assignment.setPaymentStatus(EmployeeAssignment.PaymentStatus.PAID);
            return employeeAssignmentRepository.save(assignment);
        }
        throw new RuntimeException("Assignment not found with ID: " + assignmentId);
    }
    
    /**
     * Get employee assignment count for date
     */
    public Long getEmployeeAssignmentCountForDate(Long employeeId, LocalDate date) {
        return employeeAssignmentRepository.getEmployeeAssignmentCountForDate(employeeId, date);
    }
    
    /**
     * Get total pending payment for employee
     */
    public BigDecimal getTotalPendingPaymentForEmployee(Long employeeId) {
        return employeeAssignmentRepository.getTotalPendingPaymentForEmployee(employeeId);
    }
    
    /**
     * Get total payment for employee in date range
     */
    public BigDecimal getTotalPaymentForEmployeeInDateRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        return employeeAssignmentRepository.getTotalPaymentForEmployeeInDateRange(employeeId, startDate, endDate);
    }
    
    /**
     * Get today's assignments
     */
    public List<EmployeeAssignment> getTodaysAssignments() {
        return employeeAssignmentRepository.findTodaysAssignments();
    }
}

