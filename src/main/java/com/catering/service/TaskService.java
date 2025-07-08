package com.catering.service;

import com.catering.entity.Employee;
import com.catering.entity.Order;
import com.catering.entity.Task;
import com.catering.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Task operations
 */
@Service
@Transactional
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private EmployeeService employeeService;
    
    /**
     * Save or update task
     */
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }
    
    /**
     * Find task by ID
     */
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }
    
    /**
     * Get all tasks
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    
    /**
     * Delete task
     */
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
    
    /**
     * Get tasks by status
     */
    public List<Task> getTasksByStatus(Task.TaskStatus status) {
        return taskRepository.findByTaskStatus(status);
    }
    
    /**
     * Get pending tasks
     */
    public List<Task> getPendingTasks() {
        return taskRepository.findPendingTasks();
    }
    
    /**
     * Get today's tasks
     */
    public List<Task> getTodaysTasks() {
        return taskRepository.findTodaysTasks();
    }
    
    /**
     * Get overdue tasks
     */
    public List<Task> getOverdueTasks() {
        return taskRepository.findOverdueTasks();
    }
    
    /**
     * Get urgent tasks for today
     */
    public List<Task> getUrgentTasksForToday() {
        return taskRepository.findUrgentTasksForToday();
    }
    
    /**
     * Get tasks by date range
     */
    public List<Task> getTasksByDateRange(LocalDate startDate, LocalDate endDate) {
        return taskRepository.findByTaskDateBetween(startDate, endDate);
    }
    
    /**
     * Get tasks by order
     */
    public List<Task> getTasksByOrder(Long orderId) {
        return taskRepository.findByOrderId(orderId);
    }
    
    /**
     * Get tasks by employee
     */
    public List<Task> getTasksByEmployee(Long employeeId) {
        return taskRepository.findByAssignedEmployeeId(employeeId);
    }
    
    /**
     * Search tasks by title
     */
    public List<Task> searchTasksByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTasks();
        }
        return taskRepository.findByTitleContainingIgnoreCase(keyword.trim());
    }
    
    /**
     * Create new task
     */
    public Task createTask(String title, String description, LocalDate taskDate, 
                          Task.PriorityLevel priorityLevel, Long orderId, Long employeeId) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setTaskDate(taskDate);
        task.setPriorityLevel(priorityLevel != null ? priorityLevel : Task.PriorityLevel.MEDIUM);
        task.setTaskStatus(Task.TaskStatus.PENDING);
        
        // Set order if provided
        if (orderId != null) {
            Optional<Order> orderOpt = orderService.findById(orderId);
            if (orderOpt.isPresent()) {
                task.setOrder(orderOpt.get());
            }
        }
        
        // Set assigned employee if provided
        if (employeeId != null) {
            Optional<Employee> employeeOpt = employeeService.findById(employeeId);
            if (employeeOpt.isPresent()) {
                task.setAssignedEmployee(employeeOpt.get());
            }
        }
        
        return saveTask(task);
    }
    
    /**
     * Update task
     */
    public Task updateTask(Long id, String title, String description, LocalDate taskDate,
                          Task.PriorityLevel priorityLevel, Long employeeId) {
        Optional<Task> taskOpt = findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setTitle(title);
            task.setDescription(description);
            task.setTaskDate(taskDate);
            task.setPriorityLevel(priorityLevel);
            
            // Update assigned employee if provided
            if (employeeId != null) {
                Optional<Employee> employeeOpt = employeeService.findById(employeeId);
                if (employeeOpt.isPresent()) {
                    task.setAssignedEmployee(employeeOpt.get());
                }
            } else {
                task.setAssignedEmployee(null);
            }
            
            return saveTask(task);
        }
        throw new RuntimeException("Task not found with ID: " + id);
    }
    
    /**
     * Update task status
     */
    public Task updateTaskStatus(Long id, Task.TaskStatus status) {
        Optional<Task> taskOpt = findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setTaskStatus(status);
            return saveTask(task);
        }
        throw new RuntimeException("Task not found with ID: " + id);
    }
    
    /**
     * Mark task as completed
     */
    public Task completeTask(Long id) {
        return updateTaskStatus(id, Task.TaskStatus.COMPLETED);
    }
    
    /**
     * Mark task as in progress
     */
    public Task startTask(Long id) {
        return updateTaskStatus(id, Task.TaskStatus.IN_PROGRESS);
    }
    
    /**
     * Cancel task
     */
    public Task cancelTask(Long id) {
        return updateTaskStatus(id, Task.TaskStatus.CANCELLED);
    }
    
    /**
     * Assign task to employee
     */
    public Task assignTaskToEmployee(Long taskId, Long employeeId) {
        Optional<Task> taskOpt = findById(taskId);
        Optional<Employee> employeeOpt = employeeService.findById(employeeId);
        
        if (!taskOpt.isPresent()) {
            throw new RuntimeException("Task not found with ID: " + taskId);
        }
        if (!employeeOpt.isPresent()) {
            throw new RuntimeException("Employee not found with ID: " + employeeId);
        }
        
        Task task = taskOpt.get();
        task.setAssignedEmployee(employeeOpt.get());
        
        return saveTask(task);
    }
    
    /**
     * Get pending tasks count
     */
    public Long getPendingTasksCount() {
        return taskRepository.getPendingTasksCount();
    }
    
    /**
     * Get completed tasks count for date range
     */
    public Long getCompletedTasksCount(LocalDate startDate, LocalDate endDate) {
        return taskRepository.getCompletedTasksCount(startDate, endDate);
    }
    
    /**
     * Get tasks by status and date range
     */
    public List<Task> getTasksByStatusAndDateRange(Task.TaskStatus status, LocalDate startDate, LocalDate endDate) {
        return taskRepository.findByStatusAndDateRange(status, startDate, endDate);
    }
}

