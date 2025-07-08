package com.catering.repository;

import com.catering.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Employee entity
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    /**
     * Find employees by role
     */
    List<Employee> findByRole(Employee.EmployeeRole role);
    
    /**
     * Find active employees
     */
    List<Employee> findByIsActiveTrue();
    
    /**
     * Find employees by role and active status
     */
    List<Employee> findByRoleAndIsActiveTrue(Employee.EmployeeRole role);
    
    /**
     * Search employees by name containing keyword (case insensitive)
     */
    @Query("SELECT e FROM Employee e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Employee> findByNameContainingIgnoreCase(@Param("keyword") String keyword);
    
    /**
     * Find employees by phone number
     */
    List<Employee> findByPhoneNumber(String phoneNumber);
    
    /**
     * Get employees with their assignment count for a specific date
     */
    @Query("SELECT e, COUNT(ea) FROM Employee e " +
           "LEFT JOIN e.assignments ea ON ea.assignmentDate = :date " +
           "WHERE e.isActive = true " +
           "GROUP BY e ORDER BY e.name")
    List<Object[]> findEmployeesWithAssignmentCountForDate(@Param("date") LocalDate date);
    
    /**
     * Find available employees for a specific date (not assigned to any order)
     */
    @Query("SELECT e FROM Employee e WHERE e.isActive = true " +
           "AND e.id NOT IN (SELECT ea.employee.id FROM EmployeeAssignment ea WHERE ea.assignmentDate = :date)")
    List<Employee> findAvailableEmployeesForDate(@Param("date") LocalDate date);
    
    /**
     * Find employees assigned to orders on a specific date
     */
    @Query("SELECT DISTINCT e FROM Employee e " +
           "JOIN e.assignments ea " +
           "WHERE ea.assignmentDate = :date")
    List<Employee> findEmployeesAssignedOnDate(@Param("date") LocalDate date);
    
    /**
     * Get employee assignment count for a date range
     */
    @Query("SELECT e, COUNT(ea) FROM Employee e " +
           "LEFT JOIN e.assignments ea ON ea.assignmentDate BETWEEN :startDate AND :endDate " +
           "WHERE e.isActive = true " +
           "GROUP BY e ORDER BY COUNT(ea) DESC")
    List<Object[]> findEmployeesWithAssignmentCountForDateRange(@Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate);
    
    /**
     * Search employees by name or phone number
     */
    @Query("SELECT e FROM Employee e WHERE e.isActive = true AND " +
           "(LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR e.phoneNumber LIKE CONCAT('%', :search, '%'))")
    List<Employee> searchActiveEmployees(@Param("search") String search);
}

