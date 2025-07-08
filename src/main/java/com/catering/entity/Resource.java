package com.catering.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Resource Entity for managing ingredients, equipment, and supplies
 */
@Entity
@Table(name = "resources")
public class Resource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Resource name is required")
    @Column(nullable = false)
    private String name;
    
    @NotNull(message = "Resource type is required")
    @Column(name = "resource_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;
    
    @Column(name = "unit_of_measure")
    private String unitOfMeasure;
    
    @Column(name = "unit_cost", precision = 8, scale = 2)
    private BigDecimal unitCost;
    
    @Column(name = "available_quantity")
    private Integer availableQuantity = 0;
    
    @Column(name = "minimum_stock")
    private Integer minimumStock = 0;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(length = 500)
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ResourceAssignment> assignments;
    
    // Constructors
    public Resource() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Resource(String name, ResourceType resourceType, String unitOfMeasure) {
        this();
        this.name = name;
        this.resourceType = resourceType;
        this.unitOfMeasure = unitOfMeasure;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public ResourceType getResourceType() { return resourceType; }
    public void setResourceType(ResourceType resourceType) { this.resourceType = resourceType; }
    
    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }
    
    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
    
    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }
    
    public Integer getMinimumStock() { return minimumStock; }
    public void setMinimumStock(Integer minimumStock) { this.minimumStock = minimumStock; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<ResourceAssignment> getAssignments() { return assignments; }
    public void setAssignments(List<ResourceAssignment> assignments) { this.assignments = assignments; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public enum ResourceType {
        INGREDIENT, VEGETABLE, KIRANA, EQUIPMENT, BARTAN, DISPLAY_TABLE, WATER_CAN, GAS_CYLINDER, OTHER
    }
}

