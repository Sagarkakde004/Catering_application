package com.catering.controller;

import com.catering.entity.Customer;
import com.catering.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Customer operations
 */
@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    /**
     * Get all customers
     */
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
    
    /**
     * Get customer by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);
        return customer.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create new customer
     */
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerRequest request) {
        try {
            Customer customer = customerService.createCustomer(
                request.getName(),
                request.getPhoneNumber(),
                request.getEmail(),
                request.getAddress(),
                request.getCustomerType()
            );
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update customer
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody UpdateCustomerRequest request) {
        try {
            Customer customer = customerService.updateCustomer(
                id,
                request.getName(),
                request.getPhoneNumber(),
                request.getEmail(),
                request.getAddress(),
                request.getCustomerType()
            );
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Delete customer
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Search customers
     */
    @GetMapping("/search")
    public ResponseEntity<List<Customer>> searchCustomers(@RequestParam String keyword) {
        return ResponseEntity.ok(customerService.searchCustomers(keyword));
    }
    
    /**
     * Get customers by type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Customer>> getCustomersByType(@PathVariable String type) {
        try {
            Customer.CustomerType customerType = Customer.CustomerType.valueOf(type.toUpperCase());
            return ResponseEntity.ok(customerService.getCustomersByType(customerType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get repeat customers
     */
    @GetMapping("/repeat")
    public ResponseEntity<List<Customer>> getRepeatCustomers() {
        return ResponseEntity.ok(customerService.getRepeatCustomers());
    }
    
    /**
     * Get customers ordered by order count
     */
    @GetMapping("/by-order-count")
    public ResponseEntity<List<Customer>> getCustomersOrderedByOrderCount() {
        return ResponseEntity.ok(customerService.getCustomersOrderedByOrderCount());
    }
    
    /**
     * Find customer by phone number
     */
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<Customer> findByPhoneNumber(@PathVariable String phoneNumber) {
        Optional<Customer> customer = customerService.findByPhoneNumber(phoneNumber);
        return customer.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Find customer by email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> findByEmail(@PathVariable String email) {
        Optional<Customer> customer = customerService.findByEmail(email);
        return customer.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    // Request DTOs
    public static class CreateCustomerRequest {
        private String name;
        private String phoneNumber;
        private String email;
        private String address;
        private Customer.CustomerType customerType;
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        
        public Customer.CustomerType getCustomerType() { return customerType; }
        public void setCustomerType(Customer.CustomerType customerType) { this.customerType = customerType; }
    }
    
    public static class UpdateCustomerRequest {
        private String name;
        private String phoneNumber;
        private String email;
        private String address;
        private Customer.CustomerType customerType;
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        
        public Customer.CustomerType getCustomerType() { return customerType; }
        public void setCustomerType(Customer.CustomerType customerType) { this.customerType = customerType; }
    }
}

