package com.catering.controller;

import com.catering.entity.Client;
import com.catering.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Client operations
 */
@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*")
public class ClientController {
    
    @Autowired
    private ClientService clientService;
    
    /**
     * Get all clients
     */
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }
    
    /**
     * Get active clients
     */
    @GetMapping("/active")
    public ResponseEntity<List<Client>> getActiveClients() {
        return ResponseEntity.ok(clientService.getActiveClients());
    }
    
    /**
     * Get client by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<Client> client = clientService.findById(id);
        return client.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create new client
     */
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody CreateClientRequest request) {
        try {
            Client client = clientService.createClient(
                request.getName(),
                request.getBusinessName(),
                request.getClientType(),
                request.getPhoneNumber(),
                request.getEmail(),
                request.getAddress(),
                request.getCreditLimit(),
                request.getNotes()
            );
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update client
     */
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody UpdateClientRequest request) {
        try {
            Client client = clientService.updateClient(
                id,
                request.getName(),
                request.getBusinessName(),
                request.getClientType(),
                request.getPhoneNumber(),
                request.getEmail(),
                request.getAddress(),
                request.getCreditLimit(),
                request.getNotes()
            );
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Delete client
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Deactivate client
     */
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Client> deactivateClient(@PathVariable Long id) {
        try {
            Client client = clientService.deactivateClient(id);
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get clients by type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Client>> getClientsByType(@PathVariable String type) {
        try {
            Client.ClientType clientType = Client.ClientType.valueOf(type.toUpperCase());
            return ResponseEntity.ok(clientService.getClientsByType(clientType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Search clients
     */
    @GetMapping("/search")
    public ResponseEntity<List<Client>> searchClients(@RequestParam String keyword) {
        return ResponseEntity.ok(clientService.searchClients(keyword));
    }
    
    /**
     * Get clients with outstanding balance
     */
    @GetMapping("/outstanding-balance")
    public ResponseEntity<List<Client>> getClientsWithOutstandingBalance() {
        return ResponseEntity.ok(clientService.getClientsWithOutstandingBalance());
    }
    
    /**
     * Get clients with credit balance
     */
    @GetMapping("/credit-balance")
    public ResponseEntity<List<Client>> getClientsWithCreditBalance() {
        return ResponseEntity.ok(clientService.getClientsWithCreditBalance());
    }
    
    /**
     * Get clients exceeding credit limit
     */
    @GetMapping("/exceeding-credit-limit")
    public ResponseEntity<List<Client>> getClientsExceedingCreditLimit() {
        return ResponseEntity.ok(clientService.getClientsExceedingCreditLimit());
    }
    
    /**
     * Get clients ordered by balance
     */
    @GetMapping("/by-balance")
    public ResponseEntity<List<Client>> getClientsOrderedByBalance() {
        return ResponseEntity.ok(clientService.getClientsOrderedByBalance());
    }
    
    /**
     * Update client balance
     */
    @PutMapping("/{id}/balance")
    public ResponseEntity<Client> updateClientBalance(@PathVariable Long id, @RequestBody BalanceUpdateRequest request) {
        try {
            Client client = clientService.updateClientBalance(id, request.getAmount());
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Set client balance
     */
    @PutMapping("/{id}/balance/set")
    public ResponseEntity<Client> setClientBalance(@PathVariable Long id, @RequestBody BalanceSetRequest request) {
        try {
            Client client = clientService.setClientBalance(id, request.getBalance());
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Make payment (reduce balance)
     */
    @PutMapping("/{id}/payment")
    public ResponseEntity<Client> makePayment(@PathVariable Long id, @RequestBody PaymentRequest request) {
        try {
            Client client = clientService.makePayment(id, request.getPaymentAmount());
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Add charge (increase balance)
     */
    @PutMapping("/{id}/charge")
    public ResponseEntity<Client> addCharge(@PathVariable Long id, @RequestBody ChargeRequest request) {
        try {
            Client client = clientService.addCharge(id, request.getChargeAmount());
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get total outstanding balance
     */
    @GetMapping("/total-outstanding-balance")
    public ResponseEntity<BigDecimal> getTotalOutstandingBalance() {
        return ResponseEntity.ok(clientService.getTotalOutstandingBalance());
    }
    
    // Request DTOs
    public static class CreateClientRequest {
        private String name;
        private String businessName;
        private Client.ClientType clientType;
        private String phoneNumber;
        private String email;
        private String address;
        private BigDecimal creditLimit;
        private String notes;
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getBusinessName() { return businessName; }
        public void setBusinessName(String businessName) { this.businessName = businessName; }
        
        public Client.ClientType getClientType() { return clientType; }
        public void setClientType(Client.ClientType clientType) { this.clientType = clientType; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        
        public BigDecimal getCreditLimit() { return creditLimit; }
        public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }
        
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
    
    public static class UpdateClientRequest {
        private String name;
        private String businessName;
        private Client.ClientType clientType;
        private String phoneNumber;
        private String email;
        private String address;
        private BigDecimal creditLimit;
        private String notes;
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getBusinessName() { return businessName; }
        public void setBusinessName(String businessName) { this.businessName = businessName; }
        
        public Client.ClientType getClientType() { return clientType; }
        public void setClientType(Client.ClientType clientType) { this.clientType = clientType; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        
        public BigDecimal getCreditLimit() { return creditLimit; }
        public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }
        
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
    
    public static class BalanceUpdateRequest {
        private BigDecimal amount;
        
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
    }
    
    public static class BalanceSetRequest {
        private BigDecimal balance;
        
        public BigDecimal getBalance() { return balance; }
        public void setBalance(BigDecimal balance) { this.balance = balance; }
    }
    
    public static class PaymentRequest {
        private BigDecimal paymentAmount;
        
        public BigDecimal getPaymentAmount() { return paymentAmount; }
        public void setPaymentAmount(BigDecimal paymentAmount) { this.paymentAmount = paymentAmount; }
    }
    
    public static class ChargeRequest {
        private BigDecimal chargeAmount;
        
        public BigDecimal getChargeAmount() { return chargeAmount; }
        public void setChargeAmount(BigDecimal chargeAmount) { this.chargeAmount = chargeAmount; }
    }
}

