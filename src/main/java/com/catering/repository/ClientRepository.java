package com.catering.repository;

import com.catering.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for Client entity
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    /**
     * Find clients by type
     */
    List<Client> findByClientType(Client.ClientType clientType);
    
    /**
     * Find active clients
     */
    List<Client> findByIsActiveTrue();
    
    /**
     * Find clients by type and active status
     */
    List<Client> findByClientTypeAndIsActiveTrue(Client.ClientType clientType);
    
    /**
     * Search clients by name containing keyword (case insensitive)
     */
    @Query("SELECT c FROM Client c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Client> findByNameContainingIgnoreCase(@Param("keyword") String keyword);
    
    /**
     * Search clients by business name containing keyword (case insensitive)
     */
    @Query("SELECT c FROM Client c WHERE LOWER(c.businessName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Client> findByBusinessNameContainingIgnoreCase(@Param("keyword") String keyword);
    
    /**
     * Find clients by phone number
     */
    List<Client> findByPhoneNumber(String phoneNumber);
    
    /**
     * Find clients by email
     */
    List<Client> findByEmail(String email);
    
    /**
     * Find clients with outstanding balance (positive balance)
     */
    @Query("SELECT c FROM Client c WHERE c.isActive = true AND c.currentBalance > 0")
    List<Client> findClientsWithOutstandingBalance();
    
    /**
     * Find clients with negative balance (credit balance)
     */
    @Query("SELECT c FROM Client c WHERE c.isActive = true AND c.currentBalance < 0")
    List<Client> findClientsWithCreditBalance();
    
    /**
     * Find clients exceeding credit limit
     */
    @Query("SELECT c FROM Client c WHERE c.isActive = true " +
           "AND c.currentBalance > c.creditLimit AND c.creditLimit > 0")
    List<Client> findClientsExceedingCreditLimit();
    
    /**
     * Get total outstanding balance
     */
    @Query("SELECT COALESCE(SUM(c.currentBalance), 0) FROM Client c " +
           "WHERE c.isActive = true AND c.currentBalance > 0")
    BigDecimal getTotalOutstandingBalance();
    
    /**
     * Search clients by name, business name, or phone number
     */
    @Query("SELECT c FROM Client c WHERE c.isActive = true AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(c.businessName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR c.phoneNumber LIKE CONCAT('%', :search, '%'))")
    List<Client> searchActiveClients(@Param("search") String search);
    
    /**
     * Find clients ordered by balance (highest first)
     */
    @Query("SELECT c FROM Client c WHERE c.isActive = true " +
           "ORDER BY c.currentBalance DESC")
    List<Client> findClientsOrderedByBalance();
}

