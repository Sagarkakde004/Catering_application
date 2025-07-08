package com.catering.repository;

import com.catering.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Customer entity
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Find customer by phone number
     */
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    
    /**
     * Find customer by email
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * Find customers by type
     */
    List<Customer> findByCustomerType(Customer.CustomerType customerType);
    
    /**
     * Search customers by name containing keyword (case insensitive)
     */
    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Customer> findByNameContainingIgnoreCase(@Param("keyword") String keyword);
    
    /**
     * Find customers with orders count
     */
    @Query("SELECT c FROM Customer c LEFT JOIN c.orders o GROUP BY c ORDER BY COUNT(o) DESC")
    List<Customer> findCustomersOrderedByOrderCount();
    
    /**
     * Find repeat customers (customers with more than one order)
     */
    @Query("SELECT c FROM Customer c WHERE SIZE(c.orders) > 1")
    List<Customer> findRepeatCustomers();
    
    /**
     * Find customers by name or phone number
     */
    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR c.phoneNumber LIKE CONCAT('%', :search, '%')")
    List<Customer> searchCustomers(@Param("search") String search);
}

