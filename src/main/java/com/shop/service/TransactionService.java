package com.shop.service;

import com.shop.dto.TransactionDto;
import com.shop.entity.Customer;
import com.shop.entity.Transaction;
import com.shop.entity.Transaction.TransactionStatus;
import com.shop.entity.Transaction.TransactionType;
import com.shop.entity.Transaction.PaymentMethod;
import com.shop.entity.User;
import com.shop.repository.CustomerRepository;
import com.shop.repository.TransactionRepository;
import com.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public List<TransactionDto> getAllTransactions(String userEmail) {
        return transactionRepository.findByUserEmailOrderByDateDescCreatedAtDesc(userEmail)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TransactionDto getTransactionById(String userEmail, String id) {
        Optional<Transaction> transaction = transactionRepository.findByIdAndUserEmail(id, userEmail);
        return transaction.map(this::convertToDto).orElse(null);
    }

    public List<TransactionDto> getTransactionsByCustomer(String userEmail, String customerId) {
        return transactionRepository.findByUserEmailAndCustomerIdOrderByDateDesc(userEmail, customerId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getPendingTransactions(String userEmail) {
        return transactionRepository.findByUserEmailAndStatus(userEmail, TransactionStatus.PENDING)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getOverdueTransactions(String userEmail) {
        LocalDate currentDate = LocalDate.now();
        return transactionRepository.findByUserEmailAndStatusAndDateBefore(userEmail, TransactionStatus.PENDING, currentDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TransactionDto createTransaction(String userEmail, TransactionDto transactionDto) {
        // Get user
        User user = userRepository.findActiveUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get customer and verify it belongs to the user
        Customer customer = customerRepository.findByIdAndUserEmail(transactionDto.getCustomerId(), userEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found or access denied"));

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setUser(user);
        transaction.setCustomer(customer);
        transaction.setCustomerName(customer.getName());
        transaction.setTransactionType(transactionDto.getTransactionType());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setDate(transactionDto.getDate() != null ? transactionDto.getDate() : LocalDate.now());
        transaction.setStatus(transactionDto.getStatus() != null ? transactionDto.getStatus() : TransactionStatus.PENDING);
        transaction.setPaymentMethod(transactionDto.getPaymentMethod() != null ? transactionDto.getPaymentMethod() : PaymentMethod.CASH);
        transaction.setNotes(transactionDto.getNotes());

        Transaction savedTransaction = transactionRepository.save(transaction);

        // Update customer balance based on transaction type
        updateCustomerBalance(customer, transactionDto.getTransactionType(), transactionDto.getAmount());

        return convertToDto(savedTransaction);
    }

    public TransactionDto updateTransaction(String userEmail, String id, TransactionDto transactionDto) {
        Transaction transaction = transactionRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Store old values for balance adjustment
        TransactionType oldType = transaction.getTransactionType();
        BigDecimal oldAmount = transaction.getAmount();

        // Update transaction
        transaction.setTransactionType(transactionDto.getTransactionType());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setDate(transactionDto.getDate());
        transaction.setStatus(transactionDto.getStatus());
        transaction.setPaymentMethod(transactionDto.getPaymentMethod());
        transaction.setNotes(transactionDto.getNotes());

        Transaction savedTransaction = transactionRepository.save(transaction);

        // Adjust customer balance
        Customer customer = transaction.getCustomer();
        // Reverse old transaction effect
        reverseCustomerBalance(customer, oldType, oldAmount);
        // Apply new transaction effect
        updateCustomerBalance(customer, transactionDto.getTransactionType(), transactionDto.getAmount());

        return convertToDto(savedTransaction);
    }

    public void deleteTransaction(String userEmail, String id) {
        Transaction transaction = transactionRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Reverse the transaction effect on customer balance
        Customer customer = transaction.getCustomer();
        reverseCustomerBalance(customer, transaction.getTransactionType(), transaction.getAmount());

        transactionRepository.deleteById(id);
    }

    public TransactionDto updateTransactionStatus(String userEmail, String id, TransactionStatus status) {
        Transaction transaction = transactionRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setStatus(status);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDto(savedTransaction);
    }

    // Reporting methods
    public BigDecimal getDailySales(String userEmail, LocalDate date) {
        BigDecimal sales = transactionRepository.getTotalAmountByUserAndTypeAndDateAndStatus(
                userEmail, TransactionType.CREDIT, date, TransactionStatus.COMPLETED);
        return sales != null ? sales : BigDecimal.ZERO;
    }

    public BigDecimal getDailyCashReceived(String userEmail, LocalDate date) {
        BigDecimal cash = transactionRepository.getTotalAmountByUserAndTypeAndDateAndStatus(
                userEmail, TransactionType.PAYMENT, date, TransactionStatus.COMPLETED);
        return cash != null ? cash : BigDecimal.ZERO;
    }

    public BigDecimal getDailyCreditGiven(String userEmail, LocalDate date) {
        BigDecimal credit = transactionRepository.getTotalAmountByUserAndTypeAndDateAndStatus(
                userEmail, TransactionType.CREDIT, date, TransactionStatus.PENDING);
        return credit != null ? credit : BigDecimal.ZERO;
    }

    public BigDecimal getPeriodSales(String userEmail, LocalDate startDate, LocalDate endDate) {
        BigDecimal sales = transactionRepository.getTotalAmountByUserAndTypeAndDateRangeAndStatus(
                userEmail, TransactionType.CREDIT, startDate, endDate, TransactionStatus.COMPLETED);
        return sales != null ? sales : BigDecimal.ZERO;
    }

    public BigDecimal getPeriodCashReceived(String userEmail, LocalDate startDate, LocalDate endDate) {
        BigDecimal cash = transactionRepository.getTotalAmountByUserAndTypeAndDateRangeAndStatus(
                userEmail, TransactionType.PAYMENT, startDate, endDate, TransactionStatus.COMPLETED);
        return cash != null ? cash : BigDecimal.ZERO;
    }

    public BigDecimal getPeriodCreditGiven(String userEmail, LocalDate startDate, LocalDate endDate) {
        BigDecimal credit = transactionRepository.getTotalAmountByUserAndTypeAndDateRangeAndStatus(
                userEmail, TransactionType.CREDIT, startDate, endDate, TransactionStatus.PENDING);
        return credit != null ? credit : BigDecimal.ZERO;
    }

    // Helper methods
    private void updateCustomerBalance(Customer customer, TransactionType type, BigDecimal amount) {
        BigDecimal currentBalance = customer.getTotalDue() != null ? customer.getTotalDue() : BigDecimal.ZERO;
        
        switch (type) {
            case CREDIT:
                customer.setTotalDue(currentBalance.add(amount));
                break;
            case PAYMENT:
                customer.setTotalDue(currentBalance.subtract(amount));
                break;
            case ADJUSTMENT:
                // Adjustment can be positive or negative
                customer.setTotalDue(currentBalance.subtract(amount));
                break;
        }
        
        customer.setLastTransactionDate(LocalDate.now());
        customerRepository.save(customer);
    }

    private void reverseCustomerBalance(Customer customer, TransactionType type, BigDecimal amount) {
        BigDecimal currentBalance = customer.getTotalDue() != null ? customer.getTotalDue() : BigDecimal.ZERO;
        
        switch (type) {
            case CREDIT:
                customer.setTotalDue(currentBalance.subtract(amount));
                break;
            case PAYMENT:
                customer.setTotalDue(currentBalance.add(amount));
                break;
            case ADJUSTMENT:
                customer.setTotalDue(currentBalance.add(amount));
                break;
        }
        
        customerRepository.save(customer);
    }

    private TransactionDto convertToDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setCustomerId(transaction.getCustomer().getId());
        dto.setCustomerName(transaction.getCustomerName());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());
        dto.setDate(transaction.getDate());
        dto.setStatus(transaction.getStatus());
        dto.setPaymentMethod(transaction.getPaymentMethod());
        dto.setNotes(transaction.getNotes());
        return dto;
    }
} 