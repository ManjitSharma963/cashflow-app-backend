package com.shop.controller;

import com.shop.dto.TransactionDto;
import com.shop.entity.Transaction.TransactionStatus;
import com.shop.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (customerId != null) {
            return ResponseEntity.ok(transactionService.getTransactionsByCustomer(customerId));
        }

        if (status != null && "pending".equalsIgnoreCase(status)) {
            return ResponseEntity.ok(transactionService.getPendingTransactions());
        }

        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long id) {
        TransactionDto transaction = transactionService.getTransactionById(id);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@Valid @RequestBody TransactionDto transactionDto) {
        TransactionDto createdTransaction = transactionService.createTransaction(transactionDto);
        return ResponseEntity.ok(createdTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDto> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionDto transactionDto) {
        try {
            TransactionDto updatedTransaction = transactionService.updateTransaction(id, transactionDto);
            return ResponseEntity.ok(updatedTransaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/mark-paid")
    public ResponseEntity<TransactionDto> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam TransactionStatus status) {
        try {
            TransactionDto updatedTransaction = transactionService.updateTransactionStatus(id, status);
            return ResponseEntity.ok(updatedTransaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByCustomer(@PathVariable String customerId) {
        List<TransactionDto> transactions = transactionService.getTransactionsByCustomer(customerId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<TransactionDto>> getPendingTransactions() {
        List<TransactionDto> transactions = transactionService.getPendingTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<TransactionDto>> getOverdueTransactions() {
        List<TransactionDto> transactions = transactionService.getOverdueTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/daily/sales")
    public ResponseEntity<BigDecimal> getDailySales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        BigDecimal sales = transactionService.getDailySales(date);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/daily/cash")
    public ResponseEntity<BigDecimal> getDailyCashReceived(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        BigDecimal cash = transactionService.getDailyCashReceived(date);
        return ResponseEntity.ok(cash);
    }

    @GetMapping("/daily/credit")
    public ResponseEntity<BigDecimal> getDailyCreditGiven(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        BigDecimal credit = transactionService.getDailyCreditGiven(date);
        return ResponseEntity.ok(credit);
    }

    @GetMapping("/period/sales")
    public ResponseEntity<BigDecimal> getPeriodSales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal sales = transactionService.getPeriodSales(startDate, endDate);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/period/cash")
    public ResponseEntity<BigDecimal> getPeriodCashReceived(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal cash = transactionService.getPeriodCashReceived(startDate, endDate);
        return ResponseEntity.ok(cash);
    }

    @GetMapping("/period/credit")
    public ResponseEntity<BigDecimal> getPeriodCreditGiven(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal credit = transactionService.getPeriodCreditGiven(startDate, endDate);
        return ResponseEntity.ok(credit);
    }
}