package com.catering.repository;

import com.catering.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Resource entity
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    
    /**
     * Find resources by type
     */
    List<Resource> findByResourceType(Resource.ResourceType resourceType);
    
    /**
     * Find active resources
     */
    List<Resource> findByIsActiveTrue();
    
    /**
     * Find resources by type and active status
     */
    List<Resource> findByResourceTypeAndIsActiveTrue(Resource.ResourceType resourceType);
    
    /**
     * Search resources by name containing keyword (case insensitive)
     */
    @Query("SELECT r FROM Resource r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Resource> findByNameContainingIgnoreCase(@Param("keyword") String keyword);
    
    /**
     * Find resources with low stock (available quantity <= minimum stock)
     */
    @Query("SELECT r FROM Resource r WHERE r.isActive = true " +
           "AND r.availableQuantity <= r.minimumStock")
    List<Resource> findLowStockResources();
    
    /**
     * Find resources with available quantity greater than specified amount
     */
    @Query("SELECT r FROM Resource r WHERE r.isActive = true " +
           "AND r.availableQuantity > :quantity")
    List<Resource> findResourcesWithAvailableQuantity(@Param("quantity") Integer quantity);
    
    /**
     * Find resources by unit of measure
     */
    List<Resource> findByUnitOfMeasure(String unitOfMeasure);
    
    /**
     * Get resources with their current availability
     */
    @Query("SELECT r, r.availableQuantity FROM Resource r WHERE r.isActive = true " +
           "ORDER BY r.resourceType, r.name")
    List<Object[]> findResourcesWithAvailability();
    
    /**
     * Find resources that need restocking
     */
    @Query("SELECT r FROM Resource r WHERE r.isActive = true " +
           "AND r.availableQuantity < r.minimumStock " +
           "ORDER BY r.resourceType, r.name")
    List<Resource> findResourcesNeedingRestock();
    
    /**
     * Search resources by name or description
     */
    @Query("SELECT r FROM Resource r WHERE r.isActive = true AND " +
           "(LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(r.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Resource> searchActiveResources(@Param("search") String search);
    
    /**
     * Find resources by multiple types
     */
    @Query("SELECT r FROM Resource r WHERE r.isActive = true " +
           "AND r.resourceType IN :types ORDER BY r.resourceType, r.name")
    List<Resource> findByResourceTypesAndIsActiveTrue(@Param("types") List<Resource.ResourceType> types);
}

