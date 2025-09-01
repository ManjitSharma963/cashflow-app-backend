package com.shop.repository;

import com.shop.entity.Transaction;
import com.shop.entity.Transaction.TransactionStatus;
import com.shop.entity.Transaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerId(String customerId);
    
    List<Transaction> findByCustomerIdOrderByDateDesc(String customerId);
    
    List<Transaction> findByTransactionType(TransactionType type);
    
    List<Transaction> findByStatus(TransactionStatus status);
    
    List<Transaction> findByTransactionTypeAndStatus(TransactionType type, TransactionStatus status);
    
    List<Transaction> findByTransactionTypeAndDate(TransactionType type, LocalDate date);
    
    List<Transaction> findByTransactionTypeAndDateAndStatus(TransactionType type, LocalDate date, TransactionStatus status);
    
    List<Transaction> findByTransactionTypeAndDateBetween(TransactionType type, LocalDate startDate, LocalDate endDate);
    
    List<Transaction> findByTransactionTypeAndDateBetweenAndStatus(TransactionType type, LocalDate startDate, LocalDate endDate, TransactionStatus status);
    
    List<Transaction> findByStatusAndDateBefore(TransactionStatus status, LocalDate date);
    
    @Query("SELECT t FROM Transaction t WHERE t.customer.id = :customerId AND t.status = 'PENDING' ORDER BY t.date ASC")
    List<Transaction> findPendingTransactionsByCustomer(@Param("customerId") String customerId);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.customer.id = :customerId AND t.transactionType = 'CREDIT' AND t.status = 'PENDING'")
    BigDecimal getTotalPendingCreditByCustomer(@Param("customerId") String customerId);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.customer.id = :customerId AND t.transactionType = 'PAYMENT' AND t.status = 'COMPLETED'")
    BigDecimal getTotalPaymentsByCustomer(@Param("customerId") String customerId);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.transactionType = :type AND t.date = :date AND t.status = :status")
    BigDecimal getTotalAmountByTypeAndDateAndStatus(
            @Param("type") TransactionType type, 
            @Param("date") LocalDate date, 
            @Param("status") TransactionStatus status);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.transactionType = :type AND t.date BETWEEN :startDate AND :endDate AND t.status = :status")
    BigDecimal getTotalAmountByTypeAndDateRangeAndStatus(
            @Param("type") TransactionType type, 
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate, 
            @Param("status") TransactionStatus status);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.date = :date AND t.transactionType = 'CREDIT'")
    Long getDailyCreditTransactionCount(@Param("date") LocalDate date);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.date = :date AND t.transactionType = 'PAYMENT'")
    Long getDailyPaymentTransactionCount(@Param("date") LocalDate date);
} 