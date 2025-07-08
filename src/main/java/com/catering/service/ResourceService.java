package com.catering.service;

import com.catering.entity.Order;
import com.catering.entity.Resource;
import com.catering.entity.ResourceAssignment;
import com.catering.repository.ResourceRepository;
import com.catering.repository.ResourceAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Resource operations
 */
@Service
@Transactional
public class ResourceService {
    
    @Autowired
    private ResourceRepository resourceRepository;
    
    @Autowired
    private ResourceAssignmentRepository resourceAssignmentRepository;
    
    @Autowired
    private OrderService orderService;
    
    /**
     * Save or update resource
     */
    public Resource saveResource(Resource resource) {
        return resourceRepository.save(resource);
    }
    
    /**
     * Find resource by ID
     */
    public Optional<Resource> findById(Long id) {
        return resourceRepository.findById(id);
    }
    
    /**
     * Get all resources
     */
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }
    
    /**
     * Get active resources
     */
    public List<Resource> getActiveResources() {
        return resourceRepository.findByIsActiveTrue();
    }
    
    /**
     * Delete resource
     */
    public void deleteResource(Long id) {
        resourceRepository.deleteById(id);
    }
    
    /**
     * Deactivate resource
     */
    public Resource deactivateResource(Long id) {
        Optional<Resource> resourceOpt = findById(id);
        if (resourceOpt.isPresent()) {
            Resource resource = resourceOpt.get();
            resource.setIsActive(false);
            return saveResource(resource);
        }
        throw new RuntimeException("Resource not found with ID: " + id);
    }
    
    /**
     * Get resources by type
     */
    public List<Resource> getResourcesByType(Resource.ResourceType resourceType) {
        return resourceRepository.findByResourceTypeAndIsActiveTrue(resourceType);
    }
    
    /**
     * Search resources
     */
    public List<Resource> searchResources(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getActiveResources();
        }
        return resourceRepository.searchActiveResources(keyword.trim());
    }
    
    /**
     * Get low stock resources
     */
    public List<Resource> getLowStockResources() {
        return resourceRepository.findLowStockResources();
    }
    
    /**
     * Get resources needing restock
     */
    public List<Resource> getResourcesNeedingRestock() {
        return resourceRepository.findResourcesNeedingRestock();
    }
    
    /**
     * Create new resource
     */
    public Resource createResource(String name, Resource.ResourceType resourceType, String unitOfMeasure,
                                 BigDecimal unitCost, Integer availableQuantity, Integer minimumStock, String description) {
        Resource resource = new Resource();
        resource.setName(name);
        resource.setResourceType(resourceType);
        resource.setUnitOfMeasure(unitOfMeasure);
        resource.setUnitCost(unitCost);
        resource.setAvailableQuantity(availableQuantity != null ? availableQuantity : 0);
        resource.setMinimumStock(minimumStock != null ? minimumStock : 0);
        resource.setDescription(description);
        resource.setIsActive(true);
        
        return saveResource(resource);
    }
    
    /**
     * Update resource
     */
    public Resource updateResource(Long id, String name, Resource.ResourceType resourceType, String unitOfMeasure,
                                 BigDecimal unitCost, Integer availableQuantity, Integer minimumStock, String description) {
        Optional<Resource> resourceOpt = findById(id);
        if (resourceOpt.isPresent()) {
            Resource resource = resourceOpt.get();
            resource.setName(name);
            resource.setResourceType(resourceType);
            resource.setUnitOfMeasure(unitOfMeasure);
            resource.setUnitCost(unitCost);
            resource.setAvailableQuantity(availableQuantity);
            resource.setMinimumStock(minimumStock);
            resource.setDescription(description);
            
            return saveResource(resource);
        }
        throw new RuntimeException("Resource not found with ID: " + id);
    }
    
    /**
     * Update resource quantity
     */
    public Resource updateResourceQuantity(Long id, Integer quantity) {
        Optional<Resource> resourceOpt = findById(id);
        if (resourceOpt.isPresent()) {
            Resource resource = resourceOpt.get();
            resource.setAvailableQuantity(quantity);
            return saveResource(resource);
        }
        throw new RuntimeException("Resource not found with ID: " + id);
    }
    
    /**
     * Assign resource to order
     */
    public ResourceAssignment assignResourceToOrder(Long resourceId, Long orderId, Integer quantity) {
        Optional<Resource> resourceOpt = findById(resourceId);
        Optional<Order> orderOpt = orderService.findById(orderId);
        
        if (!resourceOpt.isPresent()) {
            throw new RuntimeException("Resource not found with ID: " + resourceId);
        }
        if (!orderOpt.isPresent()) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
        
        Resource resource = resourceOpt.get();
        
        // Check availability
        if (resource.getAvailableQuantity() < quantity) {
            throw new RuntimeException("Insufficient quantity available. Available: " + resource.getAvailableQuantity() + ", Requested: " + quantity);
        }
        
        // Create assignment
        ResourceAssignment assignment = new ResourceAssignment();
        assignment.setResource(resource);
        assignment.setOrder(orderOpt.get());
        assignment.setQuantityAssigned(quantity);
        assignment.setCostPerUnit(resource.getUnitCost());
        
        // Update resource quantity
        resource.setAvailableQuantity(resource.getAvailableQuantity() - quantity);
        saveResource(resource);
        
        return resourceAssignmentRepository.save(assignment);
    }
    
    /**
     * Return resource from assignment
     */
    public ResourceAssignment returnResource(Long assignmentId, Integer quantityReturned) {
        Optional<ResourceAssignment> assignmentOpt = resourceAssignmentRepository.findById(assignmentId);
        if (assignmentOpt.isPresent()) {
            ResourceAssignment assignment = assignmentOpt.get();
            Resource resource = assignment.getResource();
            
            assignment.setIsReturned(true);
            assignment.setQuantityReturned(quantityReturned);
            
            // Update resource quantity
            resource.setAvailableQuantity(resource.getAvailableQuantity() + quantityReturned);
            saveResource(resource);
            
            return resourceAssignmentRepository.save(assignment);
        }
        throw new RuntimeException("Assignment not found with ID: " + assignmentId);
    }
    
    /**
     * Get resource assignments
     */
    public List<ResourceAssignment> getResourceAssignments(Long resourceId) {
        return resourceAssignmentRepository.findByResourceId(resourceId);
    }
    
    /**
     * Get order resource assignments
     */
    public List<ResourceAssignment> getOrderResourceAssignments(Long orderId) {
        return resourceAssignmentRepository.findByOrderId(orderId);
    }
    
    /**
     * Get unreturned assignments
     */
    public List<ResourceAssignment> getUnreturnedAssignments() {
        return resourceAssignmentRepository.findUnreturnedAssignments();
    }
    
    /**
     * Get assignments needing return
     */
    public List<ResourceAssignment> getAssignmentsNeedingReturn() {
        return resourceAssignmentRepository.findAssignmentsNeedingReturn();
    }
    
    /**
     * Get total cost for order
     */
    public BigDecimal getTotalCostForOrder(Long orderId) {
        return resourceAssignmentRepository.getTotalCostForOrder(orderId);
    }
    
    /**
     * Get resource utilization summary
     */
    public List<Object[]> getResourceUtilizationSummary() {
        return resourceAssignmentRepository.getResourceUtilizationSummary();
    }
}

