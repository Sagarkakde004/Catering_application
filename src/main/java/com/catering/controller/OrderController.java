package com.catering.controller;

import com.catering.entity.Order;
import com.catering.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Order operations
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * Get all orders
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    
    /**
     * Get order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.findById(id);
        return order.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create new order
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            Order order = orderService.createOrder(
                request.getCustomerId(),
                request.getEventDate(),
                request.getEventVenue(),
                request.getOrderType(),
                request.getGuestCount(),
                request.getNotes()
            );
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update order
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        try {
            order.setId(id);
            Order updatedOrder = orderService.saveOrder(order);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Delete order
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get orders by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String status) {
        try {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(orderService.getOrdersByStatus(orderStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get today's orders
     */
    @GetMapping("/today")
    public ResponseEntity<List<Order>> getTodaysOrders() {
        return ResponseEntity.ok(orderService.getTodaysOrders());
    }
    
    /**
     * Get pending orders
     */
    @GetMapping("/pending")
    public ResponseEntity<List<Order>> getPendingOrders() {
        return ResponseEntity.ok(orderService.getPendingOrders());
    }
    
    /**
     * Get completed orders
     */
    @GetMapping("/completed")
    public ResponseEntity<List<Order>> getCompletedOrders() {
        return ResponseEntity.ok(orderService.getCompletedOrders());
    }
    
    /**
     * Get orders by customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }
    
    /**
     * Search orders by venue
     */
    @GetMapping("/search/venue")
    public ResponseEntity<List<Order>> searchOrdersByVenue(@RequestParam String venue) {
        return ResponseEntity.ok(orderService.searchOrdersByVenue(venue));
    }
    
    /**
     * Get orders by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Order>> getOrdersByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return ResponseEntity.ok(orderService.getOrdersByDateRange(start, end));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update order status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        try {
            Order.OrderStatus status = Order.OrderStatus.valueOf(request.getStatus().toUpperCase());
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update payment status
     */
    @PutMapping("/{id}/payment-status")
    public ResponseEntity<Order> updatePaymentStatus(@PathVariable Long id, @RequestBody PaymentStatusUpdateRequest request) {
        try {
            Order.PaymentStatus paymentStatus = Order.PaymentStatus.valueOf(request.getPaymentStatus().toUpperCase());
            Order updatedOrder = orderService.updatePaymentStatus(id, paymentStatus);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update order amounts
     */
    @PutMapping("/{id}/amounts")
    public ResponseEntity<Order> updateOrderAmounts(@PathVariable Long id, @RequestBody AmountUpdateRequest request) {
        try {
            Order updatedOrder = orderService.updateOrderAmounts(
                id,
                request.getTotalAmount(),
                request.getAdvanceAmount(),
                request.getBalanceAmount()
            );
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Complete order
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<Order> completeOrder(@PathVariable Long id) {
        try {
            Order completedOrder = orderService.completeOrder(id);
            return ResponseEntity.ok(completedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Cancel order
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        try {
            Order cancelledOrder = orderService.cancelOrder(id);
            return ResponseEntity.ok(cancelledOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get monthly earnings
     */
    @GetMapping("/earnings/monthly")
    public ResponseEntity<BigDecimal> getMonthlyEarnings(@RequestParam int month, @RequestParam int year) {
        return ResponseEntity.ok(orderService.getMonthlyEarnings(month, year));
    }
    
    /**
     * Get pending orders count
     */
    @GetMapping("/count/pending")
    public ResponseEntity<Long> getPendingOrdersCount() {
        return ResponseEntity.ok(orderService.getPendingOrdersCount());
    }
    
    // Request DTOs
    public static class CreateOrderRequest {
        private Long customerId;
        private LocalDate eventDate;
        private String eventVenue;
        private Order.OrderType orderType;
        private Integer guestCount;
        private String notes;
        
        // Getters and setters
        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
        
        public LocalDate getEventDate() { return eventDate; }
        public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
        
        public String getEventVenue() { return eventVenue; }
        public void setEventVenue(String eventVenue) { this.eventVenue = eventVenue; }
        
        public Order.OrderType getOrderType() { return orderType; }
        public void setOrderType(Order.OrderType orderType) { this.orderType = orderType; }
        
        public Integer getGuestCount() { return guestCount; }
        public void setGuestCount(Integer guestCount) { this.guestCount = guestCount; }
        
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
    
    public static class StatusUpdateRequest {
        private String status;
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
    
    public static class PaymentStatusUpdateRequest {
        private String paymentStatus;
        
        public String getPaymentStatus() { return paymentStatus; }
        public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    }
    
    public static class AmountUpdateRequest {
        private BigDecimal totalAmount;
        private BigDecimal advanceAmount;
        private BigDecimal balanceAmount;
        
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        
        public BigDecimal getAdvanceAmount() { return advanceAmount; }
        public void setAdvanceAmount(BigDecimal advanceAmount) { this.advanceAmount = advanceAmount; }
        
        public BigDecimal getBalanceAmount() { return balanceAmount; }
        public void setBalanceAmount(BigDecimal balanceAmount) { this.balanceAmount = balanceAmount; }
    }
}

