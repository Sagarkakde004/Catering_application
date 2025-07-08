package com.catering.service;

import com.catering.entity.Customer;
import com.catering.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Customer operations
 */
@Service
@Transactional
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    /**
     * Save or update customer
     */
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    
    /**
     * Find customer by ID
     */
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }
    
    /**
     * Get all customers
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    /**
     * Delete customer
     */
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
    
    /**
     * Find customer by phone number
     */
    public Optional<Customer> findByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber);
    }
    
    /**
     * Find customer by email
     */
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
    
    /**
     * Search customers by keyword
     */
    public List<Customer> searchCustomers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCustomers();
        }
        return customerRepository.searchCustomers(keyword.trim());
    }
    
    /**
     * Get customers by type
     */
    public List<Customer> getCustomersByType(Customer.CustomerType customerType) {
        return customerRepository.findByCustomerType(customerType);
    }
    
    /**
     * Get repeat customers
     */
    public List<Customer> getRepeatCustomers() {
        return customerRepository.findRepeatCustomers();
    }
    
    /**
     * Get customers ordered by order count
     */
    public List<Customer> getCustomersOrderedByOrderCount() {
        return customerRepository.findCustomersOrderedByOrderCount();
    }
    
    /**
     * Check if customer exists by phone number
     */
    public boolean existsByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber).isPresent();
    }
    
    /**
     * Check if customer exists by email
     */
    public boolean existsByEmail(String email) {
        return customerRepository.findByEmail(email).isPresent();
    }
    
    /**
     * Create new customer
     */
    public Customer createCustomer(String name, String phoneNumber, String email, String address, Customer.CustomerType customerType) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhoneNumber(phoneNumber);
        customer.setEmail(email);
        customer.setAddress(address);
        customer.setCustomerType(customerType != null ? customerType : Customer.CustomerType.DIRECT);
        
        return saveCustomer(customer);
    }
    
    /**
     * Update customer information
     */
    public Customer updateCustomer(Long id, String name, String phoneNumber, String email, String address, Customer.CustomerType customerType) {
        Optional<Customer> customerOpt = findById(id);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setName(name);
            customer.setPhoneNumber(phoneNumber);
            customer.setEmail(email);
            customer.setAddress(address);
            customer.setCustomerType(customerType);
            
            return saveCustomer(customer);
        }
        throw new RuntimeException("Customer not found with ID: " + id);
    }
}

