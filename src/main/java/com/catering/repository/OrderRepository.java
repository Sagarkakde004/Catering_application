package com.catering.repository;

import com.catering.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Order entity
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Find orders by status
     */
    List<Order> findByOrderStatus(Order.OrderStatus orderStatus);
    
    /**
     * Find orders by payment status
     */
    List<Order> findByPaymentStatus(Order.PaymentStatus paymentStatus);
    
    /**
     * Find orders by order type
     */
    List<Order> findByOrderType(Order.OrderType orderType);
    
    /**
     * Find orders by event date
     */
    List<Order> findByEventDate(LocalDate eventDate);
    
    /**
     * Find orders by event date range
     */
    List<Order> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find today's orders
     */
    @Query("SELECT o FROM Order o WHERE o.eventDate = CURRENT_DATE")
    List<Order> findTodaysOrders();
    
    /**
     * Find orders by customer
     */
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId ORDER BY o.eventDate DESC")
    List<Order> findByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Find orders by venue containing keyword
     */
    @Query("SELECT o FROM Order o WHERE LOWER(o.eventVenue) LIKE LOWER(CONCAT('%', :venue, '%'))")
    List<Order> findByEventVenueContainingIgnoreCase(@Param("venue") String venue);
    
    /**
     * Get monthly earnings for a specific month and year
     */
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o " +
           "WHERE EXTRACT(MONTH FROM o.eventDate) = :month " +
           "AND EXTRACT(YEAR FROM o.eventDate) = :year " +
           "AND o.orderStatus = 'COMPLETED'")
    BigDecimal getMonthlyEarnings(@Param("month") int month, @Param("year") int year);
    
    /**
     * Get pending orders count
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus = 'PENDING'")
    Long getPendingOrdersCount();
    
    /**
     * Get completed orders count for a date range
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus = 'COMPLETED' " +
           "AND o.eventDate BETWEEN :startDate AND :endDate")
    Long getCompletedOrdersCount(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * Find orders with pending payments
     */
    @Query("SELECT o FROM Order o WHERE o.paymentStatus IN ('ADVANCE', 'BALANCE')")
    List<Order> findOrdersWithPendingPayments();
    
    /**
     * Get orders by status and date range
     */
    @Query("SELECT o FROM Order o WHERE o.orderStatus = :status " +
           "AND o.eventDate BETWEEN :startDate AND :endDate " +
           "ORDER BY o.eventDate ASC")
    List<Order> findByStatusAndDateRange(@Param("status") Order.OrderStatus status,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);
}

