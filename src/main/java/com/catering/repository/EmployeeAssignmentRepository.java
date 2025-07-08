package com.catering.repository;

import com.catering.entity.EmployeeAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for EmployeeAssignment entity
 */
@Repository
public interface EmployeeAssignmentRepository extends JpaRepository<EmployeeAssignment, Long> {
    
    /**
     * Find assignments by employee
     */
    @Query("SELECT ea FROM EmployeeAssignment ea WHERE ea.employee.id = :employeeId ORDER BY ea.assignmentDate DESC")
    List<EmployeeAssignment> findByEmployeeId(@Param("employeeId") Long employeeId);
    
    /**
     * Find assignments by order
     */
    @Query("SELECT ea FROM EmployeeAssignment ea WHERE ea.order.id = :orderId")
    List<EmployeeAssignment> findByOrderId(@Param("orderId") Long orderId);
    
    /**
     * Find assignments by date
     */
    List<EmployeeAssignment> findByAssignmentDate(LocalDate assignmentDate);
    
    /**
     * Find assignments by date range
     */
    List<EmployeeAssignment> findByAssignmentDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find assignments by payment status
     */
    List<EmployeeAssignment> findByPaymentStatus(EmployeeAssignment.PaymentStatus paymentStatus);
    
    /**
     * Find pending payments
     */
    @Query("SELECT ea FROM EmployeeAssignment ea WHERE ea.paymentStatus = 'PENDING' ORDER BY ea.assignmentDate ASC")
    List<EmployeeAssignment> findPendingPayments();
    
    /**
     * Get employee assignment count for a specific date
     */
    @Query("SELECT COUNT(ea) FROM EmployeeAssignment ea WHERE ea.employee.id = :employeeId AND ea.assignmentDate = :date")
    Long getEmployeeAssignmentCountForDate(@Param("employeeId") Long employeeId, @Param("date") LocalDate date);
    
    /**
     * Get employee assignment count for date range
     */
    @Query("SELECT COUNT(ea) FROM EmployeeAssignment ea WHERE ea.employee.id = :employeeId " +
           "AND ea.assignmentDate BETWEEN :startDate AND :endDate")
    Long getEmployeeAssignmentCountForDateRange(@Param("employeeId") Long employeeId,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
    
    /**
     * Calculate total pending payment for employee
     */
    @Query("SELECT COALESCE(SUM(ea.paymentAmount), 0) FROM EmployeeAssignment ea " +
           "WHERE ea.employee.id = :employeeId AND ea.paymentStatus = 'PENDING'")
    BigDecimal getTotalPendingPaymentForEmployee(@Param("employeeId") Long employeeId);
    
    /**
     * Calculate total payment for employee in date range
     */
    @Query("SELECT COALESCE(SUM(ea.paymentAmount), 0) FROM EmployeeAssignment ea " +
           "WHERE ea.employee.id = :employeeId " +
           "AND ea.assignmentDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalPaymentForEmployeeInDateRange(@Param("employeeId") Long employeeId,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);
    
    /**
     * Find assignments by employee and date range
     */
    @Query("SELECT ea FROM EmployeeAssignment ea WHERE ea.employee.id = :employeeId " +
           "AND ea.assignmentDate BETWEEN :startDate AND :endDate " +
           "ORDER BY ea.assignmentDate DESC")
    List<EmployeeAssignment> findByEmployeeIdAndDateRange(@Param("employeeId") Long employeeId,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);
    
    /**
     * Find today's assignments
     */
    @Query("SELECT ea FROM EmployeeAssignment ea WHERE ea.assignmentDate = CURRENT_DATE")
    List<EmployeeAssignment> findTodaysAssignments();
}

