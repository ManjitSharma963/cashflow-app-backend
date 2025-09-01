package com.shop.controller;

import com.shop.dto.CustomerDto;
import com.shop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3002" }, allowedHeaders = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String search) {

        if (search != null && !search.trim().isEmpty()) {
            return ResponseEntity.ok(customerService.searchCustomers(search));
        }

        if (category != null && !category.trim().isEmpty()) {
            return ResponseEntity.ok(customerService.getCustomersByCategory(category));
        }

        if (status != null) {
            if (status) {
                return ResponseEntity.ok(customerService.getActiveCustomers());
            } else {
                // For inactive customers, you might want to implement this method
                return ResponseEntity.ok(customerService.getAllCustomers());
            }
        }

        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable String id) {
        CustomerDto customer = customerService.getCustomerById(id);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        try {
            CustomerDto createdCustomer = customerService.createCustomer(customerDto);
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
            CustomerDto updatedCustomer = customerService.updateCustomer(id, customerDto);
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {

        try {
            customerService.deleteCustomer(id);
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
            CustomerDto updatedCustomer = customerService.updateCustomerTotalDue(id, totalDue);
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
            CustomerDto updatedCustomer = customerService.patchCustomer(id, customerDto);
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
        List<CustomerDto> customers = customerService.searchCustomers(query);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/outstanding")
    public ResponseEntity<List<CustomerDto>> getCustomersWithOutstandingBalance() {
        List<CustomerDto> customers = customerService.getCustomersWithOutstandingBalance();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/total-outstanding")
    public ResponseEntity<BigDecimal> getTotalOutstandingBalance() {
        BigDecimal total = customerService.getTotalOutstandingBalance();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<CustomerDto>> getCustomersByCategory(@PathVariable String category) {
        List<CustomerDto> customers = customerService.getCustomersByCategory(category);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CustomerDto>> getActiveCustomers() {
        List<CustomerDto> customers = customerService.getActiveCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/balance-range")
    public ResponseEntity<List<CustomerDto>> getCustomersByBalanceRange(
            @RequestParam BigDecimal minAmount,
            @RequestParam BigDecimal maxAmount) {
        List<CustomerDto> customers = customerService.getCustomersByOutstandingBalanceRange(minAmount, maxAmount);
        return ResponseEntity.ok(customers);
    }
}