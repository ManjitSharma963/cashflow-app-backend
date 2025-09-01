package com.shop.repository;

import com.shop.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

        // Basic CRUD operations (inherited from JpaRepository)
        // findById, save, deleteById, findAll are automatically available

        @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE c.mobile = :mobile")
        boolean existsByMobile(@Param("mobile") String mobile);

        @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(c.mobile) LIKE LOWER(CONCAT('%', :mobile, '%'))")
        List<Customer> findByNameContainingIgnoreCaseOrMobileContaining(@Param("name") String name,
                        @Param("mobile") String mobile);

        @Query("SELECT c FROM Customer c WHERE c.category = :category")
        List<Customer> findByCategory(@Param("category") String category);

        @Query("SELECT c FROM Customer c WHERE c.isActive = true")
        List<Customer> findByIsActiveTrue();

        @Query("SELECT c FROM Customer c WHERE c.totalDue > :amount")
        List<Customer> findByTotalDueGreaterThan(@Param("amount") BigDecimal amount);

        @Query("SELECT c FROM Customer c WHERE c.totalDue BETWEEN :minAmount AND :maxAmount")
        List<Customer> findByTotalDueBetween(@Param("minAmount") BigDecimal minAmount,
                        @Param("maxAmount") BigDecimal maxAmount);

        @Query("SELECT c FROM Customer c WHERE c.lastTransactionDate BETWEEN :startDate AND :endDate")
        List<Customer> findByLastTransactionDateBetween(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(c.totalDue) FROM Customer c WHERE c.totalDue > 0")
        BigDecimal getTotalOutstandingBalance();

        @Query("SELECT c FROM Customer c WHERE c.totalDue > 0 ORDER BY c.totalDue DESC")
        List<Customer> findTopCustomersByOutstandingBalance();

        @Query("SELECT c FROM Customer c WHERE c.lastTransactionDate < :date AND c.isActive = true")
        List<Customer> findInactiveCustomers(@Param("date") LocalDate date);

        @Query("SELECT COUNT(c) FROM Customer c WHERE c.category = :category")
        Long countByCategory(@Param("category") String category);

        @Query("SELECT c.category, COUNT(c), SUM(c.totalDue) FROM Customer c GROUP BY c.category")
        List<Object[]> getCategoryStatistics();

        // Additional utility methods that might be useful
        @Query("SELECT c FROM Customer c WHERE c.mobile = :mobile")
        Customer findByMobile(@Param("mobile") String mobile);

        @Query("SELECT c FROM Customer c WHERE c.name LIKE %:name%")
        List<Customer> findByNameContaining(@Param("name") String name);

        @Query("SELECT c FROM Customer c WHERE c.isActive = :isActive")
        List<Customer> findByIsActive(@Param("isActive") Boolean isActive);

        @Query("SELECT c FROM Customer c WHERE c.totalDue = 0")
        List<Customer> findCustomersWithNoOutstandingBalance();

        @Query("SELECT c FROM Customer c WHERE c.lastTransactionDate IS NULL")
        List<Customer> findCustomersWithNoTransactions();
}