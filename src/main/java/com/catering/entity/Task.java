package com.catering.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Task Entity for managing daily tasks and to-do items
 */
@Entity
@Table(name = "tasks")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Task title is required")
    @Column(nullable = false)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @NotNull(message = "Task date is required")
    @Column(name = "task_date", nullable = false)
    private LocalDate taskDate;
    
    @Column(name = "task_status")
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus = TaskStatus.PENDING;
    
    @Column(name = "priority_level")
    @Enumerated(EnumType.STRING)
    private PriorityLevel priorityLevel = PriorityLevel.MEDIUM;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_employee_id")
    private Employee assignedEmployee;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Task() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Task(String title, LocalDate taskDate) {
        this();
        this.title = title;
        this.taskDate = taskDate;
    }
    
    public Task(String title, String description, LocalDate taskDate, Order order) {
        this(title, taskDate);
        this.description = description;
        this.order = order;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDate getTaskDate() { return taskDate; }
    public void setTaskDate(LocalDate taskDate) { this.taskDate = taskDate; }
    
    public TaskStatus getTaskStatus() { return taskStatus; }
    public void setTaskStatus(TaskStatus taskStatus) { 
        this.taskStatus = taskStatus;
        if (taskStatus == TaskStatus.COMPLETED) {
            this.completedAt = LocalDateTime.now();
        }
    }
    
    public PriorityLevel getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(PriorityLevel priorityLevel) { this.priorityLevel = priorityLevel; }
    
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    
    public Employee getAssignedEmployee() { return assignedEmployee; }
    public void setAssignedEmployee(Employee assignedEmployee) { this.assignedEmployee = assignedEmployee; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public enum TaskStatus {
        PENDING, IN_PROGRESS, COMPLETED, CANCELLED
    }
    
    public enum PriorityLevel {
        LOW, MEDIUM, HIGH, URGENT
    }
}

