package com.catering.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Employee Entity for managing staff information
 */
@Entity
@Table(name = "employees")
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Employee name is required")
    @Column(nullable = false)
    private String name;
    
    @NotBlank(message = "Employee role is required")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeRole role;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    private String address;
    
    @Column(name = "daily_rate", precision = 8, scale = 2)
    private BigDecimal dailyRate;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmployeeAssignment> assignments;
    
    // Constructors
    public Employee() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Employee(String name, EmployeeRole role, BigDecimal dailyRate) {
        this();
        this.name = name;
        this.role = role;
        this.dailyRate = dailyRate;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public EmployeeRole getRole() { return role; }
    public void setRole(EmployeeRole role) { this.role = role; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public BigDecimal getDailyRate() { return dailyRate; }
    public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<EmployeeAssignment> getAssignments() { return assignments; }
    public void setAssignments(List<EmployeeAssignment> assignments) { this.assignments = assignments; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public enum EmployeeRole {
        COOK, WAITER, FEMALE_WORKER, CLEANER, TRANSPORT_DRIVER, BOY, SUPERVISOR, MANAGER
    }
}

