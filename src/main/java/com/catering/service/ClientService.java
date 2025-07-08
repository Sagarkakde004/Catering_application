package com.catering.service;

import com.catering.entity.Client;
import com.catering.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Client operations
 */
@Service
@Transactional
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;
    
    /**
     * Save or update client
     */
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }
    
    /**
     * Find client by ID
     */
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }
    
    /**
     * Get all clients
     */
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
    /**
     * Get active clients
     */
    public List<Client> getActiveClients() {
        return clientRepository.findByIsActiveTrue();
    }
    
    /**
     * Delete client
     */
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
    
    /**
     * Deactivate client
     */
    public Client deactivateClient(Long id) {
        Optional<Client> clientOpt = findById(id);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            client.setIsActive(false);
            return saveClient(client);
        }
        throw new RuntimeException("Client not found with ID: " + id);
    }
    
    /**
     * Get clients by type
     */
    public List<Client> getClientsByType(Client.ClientType clientType) {
        return clientRepository.findByClientTypeAndIsActiveTrue(clientType);
    }
    
    /**
     * Search clients
     */
    public List<Client> searchClients(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getActiveClients();
        }
        return clientRepository.searchActiveClients(keyword.trim());
    }
    
    /**
     * Get clients with outstanding balance
     */
    public List<Client> getClientsWithOutstandingBalance() {
        return clientRepository.findClientsWithOutstandingBalance();
    }
    
    /**
     * Get clients with credit balance
     */
    public List<Client> getClientsWithCreditBalance() {
        return clientRepository.findClientsWithCreditBalance();
    }
    
    /**
     * Get clients exceeding credit limit
     */
    public List<Client> getClientsExceedingCreditLimit() {
        return clientRepository.findClientsExceedingCreditLimit();
    }
    
    /**
     * Get clients ordered by balance
     */
    public List<Client> getClientsOrderedByBalance() {
        return clientRepository.findClientsOrderedByBalance();
    }
    
    /**
     * Create new client
     */
    public Client createClient(String name, String businessName, Client.ClientType clientType,
                              String phoneNumber, String email, String address, 
                              BigDecimal creditLimit, String notes) {
        Client client = new Client();
        client.setName(name);
        client.setBusinessName(businessName);
        client.setClientType(clientType);
        client.setPhoneNumber(phoneNumber);
        client.setEmail(email);
        client.setAddress(address);
        client.setCreditLimit(creditLimit != null ? creditLimit : BigDecimal.ZERO);
        client.setCurrentBalance(BigDecimal.ZERO);
        client.setNotes(notes);
        client.setIsActive(true);
        
        return saveClient(client);
    }
    
    /**
     * Update client
     */
    public Client updateClient(Long id, String name, String businessName, Client.ClientType clientType,
                              String phoneNumber, String email, String address, 
                              BigDecimal creditLimit, String notes) {
        Optional<Client> clientOpt = findById(id);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            client.setName(name);
            client.setBusinessName(businessName);
            client.setClientType(clientType);
            client.setPhoneNumber(phoneNumber);
            client.setEmail(email);
            client.setAddress(address);
            client.setCreditLimit(creditLimit);
            client.setNotes(notes);
            
            return saveClient(client);
        }
        throw new RuntimeException("Client not found with ID: " + id);
    }
    
    /**
     * Update client balance
     */
    public Client updateClientBalance(Long id, BigDecimal amount) {
        Optional<Client> clientOpt = findById(id);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            client.setCurrentBalance(client.getCurrentBalance().add(amount));
            return saveClient(client);
        }
        throw new RuntimeException("Client not found with ID: " + id);
    }
    
    /**
     * Set client balance
     */
    public Client setClientBalance(Long id, BigDecimal balance) {
        Optional<Client> clientOpt = findById(id);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            client.setCurrentBalance(balance);
            return saveClient(client);
        }
        throw new RuntimeException("Client not found with ID: " + id);
    }
    
    /**
     * Make payment (reduce balance)
     */
    public Client makePayment(Long id, BigDecimal paymentAmount) {
        return updateClientBalance(id, paymentAmount.negate());
    }
    
    /**
     * Add charge (increase balance)
     */
    public Client addCharge(Long id, BigDecimal chargeAmount) {
        return updateClientBalance(id, chargeAmount);
    }
    
    /**
     * Get total outstanding balance
     */
    public BigDecimal getTotalOutstandingBalance() {
        return clientRepository.getTotalOutstandingBalance();
    }
    
    /**
     * Check if client exists by phone number
     */
    public boolean existsByPhoneNumber(String phoneNumber) {
        return !clientRepository.findByPhoneNumber(phoneNumber).isEmpty();
    }
    
    /**
     * Check if client exists by email
     */
    public boolean existsByEmail(String email) {
        return !clientRepository.findByEmail(email).isEmpty();
    }
}

