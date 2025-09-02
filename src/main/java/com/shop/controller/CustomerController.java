package com.shop.controller;

import com.shop.dto.CustomerDto;
import com.shop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3002"}, allowedHeaders = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String search) {

        String userEmail = getCurrentUserEmail();

        if (search != null && !search.trim().isEmpty()) {
            return ResponseEntity.ok(customerService.searchCustomers(userEmail, search));
        }

        if (category != null && !category.trim().isEmpty()) {
            return ResponseEntity.ok(customerService.getCustomersByCategory(userEmail, category));
        }

        if (status != null) {
            if (status) {
                return ResponseEntity.ok(customerService.getActiveCustomers(userEmail));
            } else {
                return ResponseEntity.ok(customerService.getAllCustomers(userEmail));
            }
        }

        return ResponseEntity.ok(customerService.getAllCustomers(userEmail));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable String id) {
        String userEmail = getCurrentUserEmail();
        CustomerDto customer = customerService.getCustomerById(userEmail, id);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        try {
            String userEmail = getCurrentUserEmail();
            CustomerDto createdCustomer = customerService.createCustomer(userEmail, customerDto);
            return ResponseEntity.ok(createdCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(
            @PathVariable String id,
            @Valid @RequestBody CustomerDto customerDto) {
        try {
            String userEmail = getCurrentUserEmail();
            CustomerDto updatedCustomer = customerService.updateCustomer(userEmail, id, customerDto);
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        try {
            String userEmail = getCurrentUserEmail();
            customerService.deleteCustomer(userEmail, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/total-due")
    public ResponseEntity<CustomerDto> updateCustomerTotalDue(
            @PathVariable String id,
            @RequestParam BigDecimal totalDue) {
        try {
            String userEmail = getCurrentUserEmail();
            CustomerDto updatedCustomer = customerService.updateCustomerTotalDue(userEmail, id, totalDue);
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerDto> patchCustomer(
            @PathVariable String id,
            @RequestBody CustomerDto customerDto) {
        try {
            String userEmail = getCurrentUserEmail();
            CustomerDto updatedCustomer = customerService.patchCustomer(userEmail, id, customerDto);
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Object>> getCustomerTransactions(@PathVariable String id) {
        // This will be implemented when TransactionController is updated
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDto>> searchCustomers(@RequestParam String query) {
        String userEmail = getCurrentUserEmail();
        List<CustomerDto> customers = customerService.searchCustomers(userEmail, query);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/outstanding")
    public ResponseEntity<List<CustomerDto>> getCustomersWithOutstandingBalance() {
        String userEmail = getCurrentUserEmail();
        List<CustomerDto> customers = customerService.getCustomersWithOutstandingBalance(userEmail);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/total-outstanding")
    public ResponseEntity<BigDecimal> getTotalOutstandingBalance() {
        String userEmail = getCurrentUserEmail();
        BigDecimal total = customerService.getTotalOutstandingBalance(userEmail);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<CustomerDto>> getCustomersByCategory(@PathVariable String category) {
        String userEmail = getCurrentUserEmail();
        List<CustomerDto> customers = customerService.getCustomersByCategory(userEmail, category);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CustomerDto>> getActiveCustomers() {
        String userEmail = getCurrentUserEmail();
        List<CustomerDto> customers = customerService.getActiveCustomers(userEmail);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/balance-range")
    public ResponseEntity<List<CustomerDto>> getCustomersByBalanceRange(
            @RequestParam BigDecimal minAmount,
            @RequestParam BigDecimal maxAmount) {
        String userEmail = getCurrentUserEmail();
        List<CustomerDto> customers = customerService.getCustomersByOutstandingBalanceRange(userEmail, minAmount, maxAmount);
        return ResponseEntity.ok(customers);
    }
}