package com.catering.service;

import com.catering.entity.Customer;
import com.catering.entity.Order;
import com.catering.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Order operations
 */
@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CustomerService customerService;
    
    /**
     * Save or update order
     */
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
    
    /**
     * Find order by ID
     */
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
    
    /**
     * Get all orders
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    /**
     * Delete order
     */
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
    
    /**
     * Get orders by status
     */
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByOrderStatus(status);
    }
    
    /**
     * Get orders by payment status
     */
    public List<Order> getOrdersByPaymentStatus(Order.PaymentStatus paymentStatus) {
        return orderRepository.findByPaymentStatus(paymentStatus);
    }
    
    /**
     * Get orders by type
     */
    public List<Order> getOrdersByType(Order.OrderType orderType) {
        return orderRepository.findByOrderType(orderType);
    }
    
    /**
     * Get today's orders
     */
    public List<Order> getTodaysOrders() {
        return orderRepository.findTodaysOrders();
    }
    
    /**
     * Get orders by date range
     */
    public List<Order> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        return orderRepository.findByEventDateBetween(startDate, endDate);
    }
    
    /**
     * Get orders by customer
     */
    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
    
    /**
     * Search orders by venue
     */
    public List<Order> searchOrdersByVenue(String venue) {
        return orderRepository.findByEventVenueContainingIgnoreCase(venue);
    }
    
    /**
     * Get pending orders
     */
    public List<Order> getPendingOrders() {
        return getOrdersByStatus(Order.OrderStatus.PENDING);
    }
    
    /**
     * Get completed orders
     */
    public List<Order> getCompletedOrders() {
        return getOrdersByStatus(Order.OrderStatus.COMPLETED);
    }
    
    /**
     * Get cancelled orders
     */
    public List<Order> getCancelledOrders() {
        return getOrdersByStatus(Order.OrderStatus.CANCELLED);
    }
    
    /**
     * Get orders with pending payments
     */
    public List<Order> getOrdersWithPendingPayments() {
        return orderRepository.findOrdersWithPendingPayments();
    }
    
    /**
     * Create new order
     */
    public Order createOrder(Long customerId, LocalDate eventDate, String eventVenue, 
                           Order.OrderType orderType, Integer guestCount, String notes) {
        Optional<Customer> customerOpt = customerService.findById(customerId);
        if (!customerOpt.isPresent()) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        
        Order order = new Order();
        order.setCustomer(customerOpt.get());
        order.setEventDate(eventDate);
        order.setEventVenue(eventVenue);
        order.setOrderType(orderType);
        order.setGuestCount(guestCount);
        order.setNotes(notes);
        order.setOrderStatus(Order.OrderStatus.PENDING);
        order.setPaymentStatus(Order.PaymentStatus.ADVANCE);
        
        return saveOrder(order);
    }
    
    /**
     * Update order status
     */
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Optional<Order> orderOpt = findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setOrderStatus(status);
            return saveOrder(order);
        }
        throw new RuntimeException("Order not found with ID: " + orderId);
    }
    
    /**
     * Update payment status
     */
    public Order updatePaymentStatus(Long orderId, Order.PaymentStatus paymentStatus) {
        Optional<Order> orderOpt = findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setPaymentStatus(paymentStatus);
            return saveOrder(order);
        }
        throw new RuntimeException("Order not found with ID: " + orderId);
    }
    
    /**
     * Update order amounts
     */
    public Order updateOrderAmounts(Long orderId, BigDecimal totalAmount, BigDecimal advanceAmount, BigDecimal balanceAmount) {
        Optional<Order> orderOpt = findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setTotalAmount(totalAmount);
            order.setAdvanceAmount(advanceAmount);
            order.setBalanceAmount(balanceAmount);
            return saveOrder(order);
        }
        throw new RuntimeException("Order not found with ID: " + orderId);
    }
    
    /**
     * Complete order
     */
    public Order completeOrder(Long orderId) {
        return updateOrderStatus(orderId, Order.OrderStatus.COMPLETED);
    }
    
    /**
     * Cancel order
     */
    public Order cancelOrder(Long orderId) {
        return updateOrderStatus(orderId, Order.OrderStatus.CANCELLED);
    }
    
    /**
     * Get monthly earnings
     */
    public BigDecimal getMonthlyEarnings(int month, int year) {
        return orderRepository.getMonthlyEarnings(month, year);
    }
    
    /**
     * Get pending orders count
     */
    public Long getPendingOrdersCount() {
        return orderRepository.getPendingOrdersCount();
    }
    
    /**
     * Get completed orders count for date range
     */
    public Long getCompletedOrdersCount(LocalDate startDate, LocalDate endDate) {
        return orderRepository.getCompletedOrdersCount(startDate, endDate);
    }
}

