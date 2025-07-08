package com.catering.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Resource Assignment Entity for tracking resource assignments to orders
 */
@Entity
@Table(name = "resource_assignments")
public class ResourceAssignment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Resource is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;
    
    @NotNull(message = "Order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @NotNull(message = "Quantity is required")
    @Column(name = "quantity_assigned", nullable = false)
    private Integer quantityAssigned;
    
    @Column(name = "cost_per_unit", precision = 8, scale = 2)
    private BigDecimal costPerUnit;
    
    @Column(name = "total_cost", precision = 10, scale = 2)
    private BigDecimal totalCost;
    
    @Column(name = "is_returned")
    private Boolean isReturned = false;
    
    @Column(name = "quantity_returned")
    private Integer quantityReturned = 0;
    
    @Column(length = 500)
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public ResourceAssignment() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public ResourceAssignment(Resource resource, Order order, Integer quantityAssigned) {
        this();
        this.resource = resource;
        this.order = order;
        this.quantityAssigned = quantityAssigned;
        this.costPerUnit = resource.getUnitCost();
        if (this.costPerUnit != null) {
            this.totalCost = this.costPerUnit.multiply(BigDecimal.valueOf(quantityAssigned));
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Resource getResource() { return resource; }
    public void setResource(Resource resource) { this.resource = resource; }
    
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    
    public Integer getQuantityAssigned() { return quantityAssigned; }
    public void setQuantityAssigned(Integer quantityAssigned) { 
        this.quantityAssigned = quantityAssigned;
        calculateTotalCost();
    }
    
    public BigDecimal getCostPerUnit() { return costPerUnit; }
    public void setCostPerUnit(BigDecimal costPerUnit) { 
        this.costPerUnit = costPerUnit;
        calculateTotalCost();
    }
    
    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    
    public Boolean getIsReturned() { return isReturned; }
    public void setIsReturned(Boolean isReturned) { this.isReturned = isReturned; }
    
    public Integer getQuantityReturned() { return quantityReturned; }
    public void setQuantityReturned(Integer quantityReturned) { this.quantityReturned = quantityReturned; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    private void calculateTotalCost() {
        if (this.costPerUnit != null && this.quantityAssigned != null) {
            this.totalCost = this.costPerUnit.multiply(BigDecimal.valueOf(this.quantityAssigned));
        }
    }
}

