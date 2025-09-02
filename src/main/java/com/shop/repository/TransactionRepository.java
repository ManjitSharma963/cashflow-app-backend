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
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    // Find by user email
    List<Transaction> findByUserEmailOrderByDateDescCreatedAtDesc(String userEmail);
    
    Optional<Transaction> findByIdAndUserEmail(String id, String userEmail);
    
    List<Transaction> findByUserEmailAndCustomerId(String userEmail, String customerId);
    
    List<Transaction> findByUserEmailAndCustomerIdOrderByDateDesc(String userEmail, String customerId);
    
    List<Transaction> findByUserEmailAndTransactionType(String userEmail, TransactionType type);
    
    List<Transaction> findByUserEmailAndStatus(String userEmail, TransactionStatus status);
    
    List<Transaction> findByUserEmailAndTransactionTypeAndStatus(String userEmail, TransactionType type, TransactionStatus status);
    
    List<Transaction> findByUserEmailAndTransactionTypeAndDate(String userEmail, TransactionType type, LocalDate date);
    
    List<Transaction> findByUserEmailAndTransactionTypeAndDateAndStatus(String userEmail, TransactionType type, LocalDate date, TransactionStatus status);
    
    List<Transaction> findByUserEmailAndTransactionTypeAndDateBetween(String userEmail, TransactionType type, LocalDate startDate, LocalDate endDate);
    
    List<Transaction> findByUserEmailAndTransactionTypeAndDateBetweenAndStatus(String userEmail, TransactionType type, LocalDate startDate, LocalDate endDate, TransactionStatus status);
    
    List<Transaction> findByUserEmailAndStatusAndDateBefore(String userEmail, TransactionStatus status, LocalDate date);
    
    List<Transaction> findByUserEmailAndDateBetween(String userEmail, LocalDate startDate, LocalDate endDate);
    
    // Custom queries with user filtering
    @Query("SELECT t FROM Transaction t WHERE t.user.email = :userEmail AND t.customer.id = :customerId AND t.status = 'PENDING' ORDER BY t.date ASC")
    List<Transaction> findPendingTransactionsByCustomerAndUser(@Param("userEmail") String userEmail, @Param("customerId") String customerId);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.email = :userEmail AND t.customer.id = :customerId AND t.transactionType = 'CREDIT' AND t.status = 'PENDING'")
    BigDecimal getTotalPendingCreditByCustomerAndUser(@Param("userEmail") String userEmail, @Param("customerId") String customerId);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.email = :userEmail AND t.customer.id = :customerId AND t.transactionType = 'PAYMENT' AND t.status = 'COMPLETED'")
    BigDecimal getTotalPaymentsByCustomerAndUser(@Param("userEmail") String userEmail, @Param("customerId") String customerId);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.email = :userEmail AND t.transactionType = :type AND t.date = :date AND t.status = :status")
    BigDecimal getTotalAmountByUserAndTypeAndDateAndStatus(
            @Param("userEmail") String userEmail,
            @Param("type") TransactionType type, 
            @Param("date") LocalDate date, 
            @Param("status") TransactionStatus status);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.email = :userEmail AND t.transactionType = :type AND t.date BETWEEN :startDate AND :endDate AND t.status = :status")
    BigDecimal getTotalAmountByUserAndTypeAndDateRangeAndStatus(
            @Param("userEmail") String userEmail,
            @Param("type") TransactionType type, 
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate, 
            @Param("status") TransactionStatus status);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user.email = :userEmail AND t.date = :date AND t.transactionType = 'CREDIT'")
    Long getDailyCreditTransactionCountByUser(@Param("userEmail") String userEmail, @Param("date") LocalDate date);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user.email = :userEmail AND t.date = :date AND t.transactionType = 'PAYMENT'")
    Long getDailyPaymentTransactionCountByUser(@Param("userEmail") String userEmail, @Param("date") LocalDate date);
    
    // Aggregation queries for user
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user.email = :userEmail AND t.status = 'PENDING'")
    Long countPendingTransactionsByUser(@Param("userEmail") String userEmail);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user.email = :userEmail AND t.status = 'COMPLETED'")
    Long countCompletedTransactionsByUser(@Param("userEmail") String userEmail);
} 