package com.catering.repository;

import com.catering.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Task entity
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    /**
     * Find tasks by status
     */
    List<Task> findByTaskStatus(Task.TaskStatus taskStatus);
    
    /**
     * Find tasks by date
     */
    List<Task> findByTaskDate(LocalDate taskDate);
    
    /**
     * Find tasks by date range
     */
    List<Task> findByTaskDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find today's tasks
     */
    @Query("SELECT t FROM Task t WHERE t.taskDate = CURRENT_DATE ORDER BY t.priorityLevel DESC, t.createdAt ASC")
    List<Task> findTodaysTasks();
    
    /**
     * Find pending tasks
     */
    @Query("SELECT t FROM Task t WHERE t.taskStatus = 'PENDING' ORDER BY t.taskDate ASC, t.priorityLevel DESC")
    List<Task> findPendingTasks();
    
    /**
     * Find tasks by order
     */
    @Query("SELECT t FROM Task t WHERE t.order.id = :orderId ORDER BY t.taskDate ASC")
    List<Task> findByOrderId(@Param("orderId") Long orderId);
    
    /**
     * Find tasks assigned to employee
     */
    @Query("SELECT t FROM Task t WHERE t.assignedEmployee.id = :employeeId ORDER BY t.taskDate ASC")
    List<Task> findByAssignedEmployeeId(@Param("employeeId") Long employeeId);
    
    /**
     * Find tasks by priority level
     */
    List<Task> findByPriorityLevel(Task.PriorityLevel priorityLevel);
    
    /**
     * Get pending tasks count
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.taskStatus = 'PENDING'")
    Long getPendingTasksCount();
    
    /**
     * Get completed tasks count for date range
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.taskStatus = 'COMPLETED' " +
           "AND t.taskDate BETWEEN :startDate AND :endDate")
    Long getCompletedTasksCount(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * Find overdue tasks (pending tasks with task date before today)
     */
    @Query("SELECT t FROM Task t WHERE t.taskStatus = 'PENDING' " +
           "AND t.taskDate < CURRENT_DATE ORDER BY t.taskDate ASC")
    List<Task> findOverdueTasks();
    
    /**
     * Find tasks by status and date range
     */
    @Query("SELECT t FROM Task t WHERE t.taskStatus = :status " +
           "AND t.taskDate BETWEEN :startDate AND :endDate " +
           "ORDER BY t.taskDate ASC, t.priorityLevel DESC")
    List<Task> findByStatusAndDateRange(@Param("status") Task.TaskStatus status,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);
    
    /**
     * Search tasks by title containing keyword
     */
    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY t.taskDate ASC")
    List<Task> findByTitleContainingIgnoreCase(@Param("keyword") String keyword);
    
    /**
     * Find urgent tasks for today
     */
    @Query("SELECT t FROM Task t WHERE t.taskDate = CURRENT_DATE " +
           "AND t.priorityLevel = 'URGENT' AND t.taskStatus = 'PENDING'")
    List<Task> findUrgentTasksForToday();
}

