package com.catering.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Order Entity for managing catering orders
 */
@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @NotNull(message = "Event date is required")
    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;
    
    @NotBlank(message = "Event venue is required")
    @Column(name = "event_venue", nullable = false)
    private String eventVenue;
    
    @NotNull(message = "Order type is required")
    @Column(name = "order_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PENDING;
    
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.ADVANCE;
    
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(name = "advance_amount", precision = 10, scale = 2)
    private BigDecimal advanceAmount;
    
    @Column(name = "balance_amount", precision = 10, scale = 2)
    private BigDecimal balanceAmount;
    
    @Column(name = "guest_count")
    private Integer guestCount;
    
    @Column(length = 1000)
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ResourceAssignment> resourceAssignments;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmployeeAssignment> employeeAssignments;
    
    // Constructors
    public Order() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Order(Customer customer, LocalDate eventDate, String eventVenue, OrderType orderType) {
        this();
        this.customer = customer;
        this.eventDate = eventDate;
        this.eventVenue = eventVenue;
        this.orderType = orderType;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    
    public String getEventVenue() { return eventVenue; }
    public void setEventVenue(String eventVenue) { this.eventVenue = eventVenue; }
    
    public OrderType getOrderType() { return orderType; }
    public void setOrderType(OrderType orderType) { this.orderType = orderType; }
    
    public OrderStatus getOrderStatus() { return orderStatus; }
    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }
    
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public BigDecimal getAdvanceAmount() { return advanceAmount; }
    public void setAdvanceAmount(BigDecimal advanceAmount) { this.advanceAmount = advanceAmount; }
    
    public BigDecimal getBalanceAmount() { return balanceAmount; }
    public void setBalanceAmount(BigDecimal balanceAmount) { this.balanceAmount = balanceAmount; }
    
    public Integer getGuestCount() { return guestCount; }
    public void setGuestCount(Integer guestCount) { this.guestCount = guestCount; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
    
    public List<ResourceAssignment> getResourceAssignments() { return resourceAssignments; }
    public void setResourceAssignments(List<ResourceAssignment> resourceAssignments) { this.resourceAssignments = resourceAssignments; }
    
    public List<EmployeeAssignment> getEmployeeAssignments() { return employeeAssignments; }
    public void setEmployeeAssignments(List<EmployeeAssignment> employeeAssignments) { this.employeeAssignments = employeeAssignments; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public enum OrderType {
        FULL_CATERING, HALF_CATERING
    }
    
    public enum OrderStatus {
        PENDING, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED
    }
    
    public enum PaymentStatus {
        ADVANCE, BALANCE, COMPLETE
    }
}

