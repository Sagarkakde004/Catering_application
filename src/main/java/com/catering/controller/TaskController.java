package com.catering.controller;

import com.catering.entity.Task;
import com.catering.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Task operations
 */
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    /**
     * Get all tasks
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }
    
    /**
     * Get task by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.findById(id);
        return task.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create new task
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskRequest request) {
        try {
            Task task = taskService.createTask(
                request.getTitle(),
                request.getDescription(),
                request.getTaskDate(),
                request.getPriorityLevel(),
                request.getOrderId(),
                request.getEmployeeId()
            );
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update task
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        try {
            Task task = taskService.updateTask(
                id,
                request.getTitle(),
                request.getDescription(),
                request.getTaskDate(),
                request.getPriorityLevel(),
                request.getEmployeeId()
            );
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Delete task
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get tasks by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable String status) {
        try {
            Task.TaskStatus taskStatus = Task.TaskStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(taskService.getTasksByStatus(taskStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get pending tasks
     */
    @GetMapping("/pending")
    public ResponseEntity<List<Task>> getPendingTasks() {
        return ResponseEntity.ok(taskService.getPendingTasks());
    }
    
    /**
     * Get today's tasks
     */
    @GetMapping("/today")
    public ResponseEntity<List<Task>> getTodaysTasks() {
        return ResponseEntity.ok(taskService.getTodaysTasks());
    }
    
    /**
     * Get overdue tasks
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<Task>> getOverdueTasks() {
        return ResponseEntity.ok(taskService.getOverdueTasks());
    }
    
    /**
     * Get urgent tasks for today
     */
    @GetMapping("/urgent/today")
    public ResponseEntity<List<Task>> getUrgentTasksForToday() {
        return ResponseEntity.ok(taskService.getUrgentTasksForToday());
    }
    
    /**
     * Get tasks by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Task>> getTasksByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return ResponseEntity.ok(taskService.getTasksByDateRange(start, end));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get tasks by order
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Task>> getTasksByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(taskService.getTasksByOrder(orderId));
    }
    
    /**
     * Get tasks by employee
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Task>> getTasksByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(taskService.getTasksByEmployee(employeeId));
    }
    
    /**
     * Search tasks by title
     */
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasksByTitle(@RequestParam String keyword) {
        return ResponseEntity.ok(taskService.searchTasksByTitle(keyword));
    }
    
    /**
     * Update task status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestBody TaskStatusUpdateRequest request) {
        try {
            Task.TaskStatus status = Task.TaskStatus.valueOf(request.getStatus().toUpperCase());
            Task updatedTask = taskService.updateTaskStatus(id, status);
            return ResponseEntity.ok(updatedTask);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Complete task
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable Long id) {
        try {
            Task completedTask = taskService.completeTask(id);
            return ResponseEntity.ok(completedTask);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Start task
     */
    @PutMapping("/{id}/start")
    public ResponseEntity<Task> startTask(@PathVariable Long id) {
        try {
            Task startedTask = taskService.startTask(id);
            return ResponseEntity.ok(startedTask);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Cancel task
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Task> cancelTask(@PathVariable Long id) {
        try {
            Task cancelledTask = taskService.cancelTask(id);
            return ResponseEntity.ok(cancelledTask);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Assign task to employee
     */
    @PutMapping("/{taskId}/assign/{employeeId}")
    public ResponseEntity<Task> assignTaskToEmployee(@PathVariable Long taskId, @PathVariable Long employeeId) {
        try {
            Task assignedTask = taskService.assignTaskToEmployee(taskId, employeeId);
            return ResponseEntity.ok(assignedTask);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get pending tasks count
     */
    @GetMapping("/count/pending")
    public ResponseEntity<Long> getPendingTasksCount() {
        return ResponseEntity.ok(taskService.getPendingTasksCount());
    }
    
    /**
     * Get completed tasks count for date range
     */
    @GetMapping("/count/completed")
    public ResponseEntity<Long> getCompletedTasksCount(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            Long count = taskService.getCompletedTasksCount(start, end);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Request DTOs
    public static class CreateTaskRequest {
        private String title;
        private String description;
        private LocalDate taskDate;
        private Task.PriorityLevel priorityLevel;
        private Long orderId;
        private Long employeeId;
        
        // Getters and setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public LocalDate getTaskDate() { return taskDate; }
        public void setTaskDate(LocalDate taskDate) { this.taskDate = taskDate; }
        
        public Task.PriorityLevel getPriorityLevel() { return priorityLevel; }
        public void setPriorityLevel(Task.PriorityLevel priorityLevel) { this.priorityLevel = priorityLevel; }
        
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        
        public Long getEmployeeId() { return employeeId; }
        public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    }
    
    public static class UpdateTaskRequest {
        private String title;
        private String description;
        private LocalDate taskDate;
        private Task.PriorityLevel priorityLevel;
        private Long employeeId;
        
        // Getters and setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public LocalDate getTaskDate() { return taskDate; }
        public void setTaskDate(LocalDate taskDate) { this.taskDate = taskDate; }
        
        public Task.PriorityLevel getPriorityLevel() { return priorityLevel; }
        public void setPriorityLevel(Task.PriorityLevel priorityLevel) { this.priorityLevel = priorityLevel; }
        
        public Long getEmployeeId() { return employeeId; }
        public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    }
    
    public static class TaskStatusUpdateRequest {
        private String status;
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}

