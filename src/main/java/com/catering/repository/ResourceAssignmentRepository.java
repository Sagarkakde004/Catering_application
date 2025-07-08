package com.catering.repository;

import com.catering.entity.ResourceAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for ResourceAssignment entity
 */
@Repository
public interface ResourceAssignmentRepository extends JpaRepository<ResourceAssignment, Long> {
    
    /**
     * Find assignments by resource
     */
    @Query("SELECT ra FROM ResourceAssignment ra WHERE ra.resource.id = :resourceId ORDER BY ra.createdAt DESC")
    List<ResourceAssignment> findByResourceId(@Param("resourceId") Long resourceId);
    
    /**
     * Find assignments by order
     */
    @Query("SELECT ra FROM ResourceAssignment ra WHERE ra.order.id = :orderId")
    List<ResourceAssignment> findByOrderId(@Param("orderId") Long orderId);
    
    /**
     * Find assignments by return status
     */
    List<ResourceAssignment> findByIsReturned(Boolean isReturned);
    
    /**
     * Find unreturned assignments
     */
    @Query("SELECT ra FROM ResourceAssignment ra WHERE ra.isReturned = false ORDER BY ra.createdAt ASC")
    List<ResourceAssignment> findUnreturnedAssignments();
    
    /**
     * Get total assigned quantity for a resource
     */
    @Query("SELECT COALESCE(SUM(ra.quantityAssigned), 0) FROM ResourceAssignment ra " +
           "WHERE ra.resource.id = :resourceId AND ra.isReturned = false")
    Integer getTotalAssignedQuantityForResource(@Param("resourceId") Long resourceId);
    
    /**
     * Get total cost for an order
     */
    @Query("SELECT COALESCE(SUM(ra.totalCost), 0) FROM ResourceAssignment ra WHERE ra.order.id = :orderId")
    BigDecimal getTotalCostForOrder(@Param("orderId") Long orderId);
    
    /**
     * Find assignments by resource type
     */
    @Query("SELECT ra FROM ResourceAssignment ra WHERE ra.resource.resourceType = :resourceType")
    List<ResourceAssignment> findByResourceType(@Param("resourceType") com.catering.entity.Resource.ResourceType resourceType);
    
    /**
     * Find assignments that need to be returned (unreturned assignments for completed orders)
     */
    @Query("SELECT ra FROM ResourceAssignment ra " +
           "WHERE ra.isReturned = false " +
           "AND ra.order.orderStatus = 'COMPLETED' " +
           "ORDER BY ra.createdAt ASC")
    List<ResourceAssignment> findAssignmentsNeedingReturn();
    
    /**
     * Get resource utilization summary
     */
    @Query("SELECT ra.resource, COUNT(ra), SUM(ra.quantityAssigned) FROM ResourceAssignment ra " +
           "GROUP BY ra.resource ORDER BY COUNT(ra) DESC")
    List<Object[]> getResourceUtilizationSummary();
    
    /**
     * Find assignments by order and resource type
     */
    @Query("SELECT ra FROM ResourceAssignment ra " +
           "WHERE ra.order.id = :orderId AND ra.resource.resourceType = :resourceType")
    List<ResourceAssignment> findByOrderIdAndResourceType(@Param("orderId") Long orderId,
                                                          @Param("resourceType") com.catering.entity.Resource.ResourceType resourceType);
}

