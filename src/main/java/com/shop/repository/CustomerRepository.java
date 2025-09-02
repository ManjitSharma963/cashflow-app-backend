package com.shop.repository;

import com.shop.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    
    // Find by user email
    List<Customer> findByUserEmailAndIsActiveTrue(String userEmail);
    
    Optional<Customer> findByIdAndUserEmail(String id, String userEmail);
    
    boolean existsByUserEmailAndMobile(String userEmail, String mobile);
    
    // Search methods
    List<Customer> findByUserEmailAndNameContainingIgnoreCaseOrMobileContaining(String userEmail, String name, String mobile);
    
    List<Customer> findByUserEmailAndCategory(String userEmail, String category);
    
    List<Customer> findByUserEmailAndTotalDueGreaterThan(String userEmail, BigDecimal amount);
    
    List<Customer> findByUserEmailAndTotalDueBetween(String userEmail, BigDecimal minAmount, BigDecimal maxAmount);
    
    List<Customer> findByUserEmailAndLastTransactionDateBetween(String userEmail, LocalDate startDate, LocalDate endDate);
    
    // Aggregation queries
    @Query("SELECT SUM(c.totalDue) FROM Customer c WHERE c.user.email = :userEmail AND c.isActive = true")
    BigDecimal getTotalOutstandingBalanceByUser(@Param("userEmail") String userEmail);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.user.email = :userEmail AND c.isActive = true")
    Long countActiveCustomersByUser(@Param("userEmail") String userEmail);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.user.email = :userEmail AND c.totalDue > 0")
    Long countCustomersWithOutstandingBalanceByUser(@Param("userEmail") String userEmail);
}