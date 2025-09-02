package com.shop.controller;

import com.shop.dto.TransactionDto;
import com.shop.entity.Transaction.TransactionStatus;
import com.shop.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3002"}, allowedHeaders = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String userEmail = getCurrentUserEmail();

        if (customerId != null) {
            return ResponseEntity.ok(transactionService.getTransactionsByCustomer(userEmail, customerId));
        }

        if (status != null && "pending".equalsIgnoreCase(status)) {
            return ResponseEntity.ok(transactionService.getPendingTransactions(userEmail));
        }

        return ResponseEntity.ok(transactionService.getAllTransactions(userEmail));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable String id) {
        String userEmail = getCurrentUserEmail();
        TransactionDto transaction = transactionService.getTransactionById(userEmail, id);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@Valid @RequestBody TransactionDto transactionDto) {
        String userEmail = getCurrentUserEmail();
        TransactionDto createdTransaction = transactionService.createTransaction(userEmail, transactionDto);
        return ResponseEntity.ok(createdTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDto> updateTransaction(
            @PathVariable String id,
            @Valid @RequestBody TransactionDto transactionDto) {
        try {
            String userEmail = getCurrentUserEmail();
            TransactionDto updatedTransaction = transactionService.updateTransaction(userEmail, id, transactionDto);
            return ResponseEntity.ok(updatedTransaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        try {
            String userEmail = getCurrentUserEmail();
            transactionService.deleteTransaction(userEmail, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/mark-paid")
    public ResponseEntity<TransactionDto> updatePaymentStatus(
            @PathVariable String id,
            @RequestParam TransactionStatus status) {
        try {
            String userEmail = getCurrentUserEmail();
            TransactionDto updatedTransaction = transactionService.updateTransactionStatus(userEmail, id, status);
            return ResponseEntity.ok(updatedTransaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByCustomer(@PathVariable String customerId) {
        String userEmail = getCurrentUserEmail();
        List<TransactionDto> transactions = transactionService.getTransactionsByCustomer(userEmail, customerId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<TransactionDto>> getPendingTransactions() {
        String userEmail = getCurrentUserEmail();
        List<TransactionDto> transactions = transactionService.getPendingTransactions(userEmail);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<TransactionDto>> getOverdueTransactions() {
        String userEmail = getCurrentUserEmail();
        List<TransactionDto> transactions = transactionService.getOverdueTransactions(userEmail);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/daily/sales")
    public ResponseEntity<BigDecimal> getDailySales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String userEmail = getCurrentUserEmail();
        BigDecimal sales = transactionService.getDailySales(userEmail, date);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/daily/cash")
    public ResponseEntity<BigDecimal> getDailyCashReceived(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String userEmail = getCurrentUserEmail();
        BigDecimal cash = transactionService.getDailyCashReceived(userEmail, date);
        return ResponseEntity.ok(cash);
    }

    @GetMapping("/daily/credit")
    public ResponseEntity<BigDecimal> getDailyCreditGiven(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String userEmail = getCurrentUserEmail();
        BigDecimal credit = transactionService.getDailyCreditGiven(userEmail, date);
        return ResponseEntity.ok(credit);
    }

    @GetMapping("/period/sales")
    public ResponseEntity<BigDecimal> getPeriodSales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        String userEmail = getCurrentUserEmail();
        BigDecimal sales = transactionService.getPeriodSales(userEmail, startDate, endDate);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/period/cash")
    public ResponseEntity<BigDecimal> getPeriodCashReceived(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        String userEmail = getCurrentUserEmail();
        BigDecimal cash = transactionService.getPeriodCashReceived(userEmail, startDate, endDate);
        return ResponseEntity.ok(cash);
    }

    @GetMapping("/period/credit")
    public ResponseEntity<BigDecimal> getPeriodCreditGiven(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        String userEmail = getCurrentUserEmail();
        BigDecimal credit = transactionService.getPeriodCreditGiven(userEmail, startDate, endDate);
        return ResponseEntity.ok(credit);
    }
}