package com.catering.controller;

import com.catering.entity.Resource;
import com.catering.entity.ResourceAssignment;
import com.catering.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Resource operations
 */
@RestController
@RequestMapping("/api/resources")
@CrossOrigin(origins = "*")
public class ResourceController {
    
    @Autowired
    private ResourceService resourceService;
    
    /**
     * Get all resources
     */
    @GetMapping
    public ResponseEntity<List<Resource>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAllResources());
    }
    
    /**
     * Get active resources
     */
    @GetMapping("/active")
    public ResponseEntity<List<Resource>> getActiveResources() {
        return ResponseEntity.ok(resourceService.getActiveResources());
    }
    
    /**
     * Get resource by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResourceById(@PathVariable Long id) {
        Optional<Resource> resource = resourceService.findById(id);
        return resource.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create new resource
     */
    @PostMapping
    public ResponseEntity<Resource> createResource(@RequestBody CreateResourceRequest request) {
        try {
            Resource resource = resourceService.createResource(
                request.getName(),
                request.getResourceType(),
                request.getUnitOfMeasure(),
                request.getUnitCost(),
                request.getAvailableQuantity(),
                request.getMinimumStock(),
                request.getDescription()
            );
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update resource
     */
    @PutMapping("/{id}")
    public ResponseEntity<Resource> updateResource(@PathVariable Long id, @RequestBody UpdateResourceRequest request) {
        try {
            Resource resource = resourceService.updateResource(
                id,
                request.getName(),
                request.getResourceType(),
                request.getUnitOfMeasure(),
                request.getUnitCost(),
                request.getAvailableQuantity(),
                request.getMinimumStock(),
                request.getDescription()
            );
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Delete resource
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        try {
            resourceService.deleteResource(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get resources by type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Resource>> getResourcesByType(@PathVariable String type) {
        try {
            Resource.ResourceType resourceType = Resource.ResourceType.valueOf(type.toUpperCase());
            return ResponseEntity.ok(resourceService.getResourcesByType(resourceType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Search resources
     */
    @GetMapping("/search")
    public ResponseEntity<List<Resource>> searchResources(@RequestParam String keyword) {
        return ResponseEntity.ok(resourceService.searchResources(keyword));
    }
    
    /**
     * Get low stock resources
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<Resource>> getLowStockResources() {
        return ResponseEntity.ok(resourceService.getLowStockResources());
    }
    
    /**
     * Get resources needing restock
     */
    @GetMapping("/restock-needed")
    public ResponseEntity<List<Resource>> getResourcesNeedingRestock() {
        return ResponseEntity.ok(resourceService.getResourcesNeedingRestock());
    }
    
    /**
     * Update resource quantity
     */
    @PutMapping("/{id}/quantity")
    public ResponseEntity<Resource> updateResourceQuantity(@PathVariable Long id, @RequestBody QuantityUpdateRequest request) {
        try {
            Resource resource = resourceService.updateResourceQuantity(id, request.getQuantity());
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Assign resource to order
     */
    @PostMapping("/{resourceId}/assign")
    public ResponseEntity<ResourceAssignment> assignResourceToOrder(
            @PathVariable Long resourceId,
            @RequestBody ResourceAssignmentRequest request) {
        try {
            ResourceAssignment assignment = resourceService.assignResourceToOrder(
                resourceId,
                request.getOrderId(),
                request.getQuantity()
            );
            return ResponseEntity.ok(assignment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Return resource from assignment
     */
    @PutMapping("/assignments/{assignmentId}/return")
    public ResponseEntity<ResourceAssignment> returnResource(
            @PathVariable Long assignmentId,
            @RequestBody ReturnResourceRequest request) {
        try {
            ResourceAssignment assignment = resourceService.returnResource(assignmentId, request.getQuantityReturned());
            return ResponseEntity.ok(assignment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get resource assignments
     */
    @GetMapping("/{id}/assignments")
    public ResponseEntity<List<ResourceAssignment>> getResourceAssignments(@PathVariable Long id) {
        return ResponseEntity.ok(resourceService.getResourceAssignments(id));
    }
    
    /**
     * Get unreturned assignments
     */
    @GetMapping("/assignments/unreturned")
    public ResponseEntity<List<ResourceAssignment>> getUnreturnedAssignments() {
        return ResponseEntity.ok(resourceService.getUnreturnedAssignments());
    }
    
    /**
     * Get assignments needing return
     */
    @GetMapping("/assignments/need-return")
    public ResponseEntity<List<ResourceAssignment>> getAssignmentsNeedingReturn() {
        return ResponseEntity.ok(resourceService.getAssignmentsNeedingReturn());
    }
    
    /**
     * Get resource utilization summary
     */
    @GetMapping("/utilization")
    public ResponseEntity<List<Object[]>> getResourceUtilizationSummary() {
        return ResponseEntity.ok(resourceService.getResourceUtilizationSummary());
    }
    
    // Request DTOs
    public static class CreateResourceRequest {
        private String name;
        private Resource.ResourceType resourceType;
        private String unitOfMeasure;
        private BigDecimal unitCost;
        private Integer availableQuantity;
        private Integer minimumStock;
        private String description;
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Resource.ResourceType getResourceType() { return resourceType; }
        public void setResourceType(Resource.ResourceType resourceType) { this.resourceType = resourceType; }
        
        public String getUnitOfMeasure() { return unitOfMeasure; }
        public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }
        
        public BigDecimal getUnitCost() { return unitCost; }
        public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
        
        public Integer getAvailableQuantity() { return availableQuantity; }
        public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }
        
        public Integer getMinimumStock() { return minimumStock; }
        public void setMinimumStock(Integer minimumStock) { this.minimumStock = minimumStock; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
    
    public static class UpdateResourceRequest {
        private String name;
        private Resource.ResourceType resourceType;
        private String unitOfMeasure;
        private BigDecimal unitCost;
        private Integer availableQuantity;
        private Integer minimumStock;
        private String description;
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Resource.ResourceType getResourceType() { return resourceType; }
        public void setResourceType(Resource.ResourceType resourceType) { this.resourceType = resourceType; }
        
        public String getUnitOfMeasure() { return unitOfMeasure; }
        public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }
        
        public BigDecimal getUnitCost() { return unitCost; }
        public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
        
        public Integer getAvailableQuantity() { return availableQuantity; }
        public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }
        
        public Integer getMinimumStock() { return minimumStock; }
        public void setMinimumStock(Integer minimumStock) { this.minimumStock = minimumStock; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
    
    public static class QuantityUpdateRequest {
        private Integer quantity;
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
    
    public static class ResourceAssignmentRequest {
        private Long orderId;
        private Integer quantity;
        
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
    
    public static class ReturnResourceRequest {
        private Integer quantityReturned;
        
        public Integer getQuantityReturned() { return quantityReturned; }
        public void setQuantityReturned(Integer quantityReturned) { this.quantityReturned = quantityReturned; }
    }
}

