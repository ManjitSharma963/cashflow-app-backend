package com.shop.service;

import com.shop.dto.TransactionDto;
import com.shop.entity.Customer;
import com.shop.entity.Transaction;
import com.shop.entity.Transaction.TransactionStatus;
import com.shop.entity.Transaction.TransactionType;
import com.shop.repository.CustomerRepository;
import com.shop.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TransactionDto getTransactionById(Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        return transaction.map(this::convertToDto).orElse(null);
    }

    public TransactionDto createTransaction(TransactionDto transactionDto) {
        Customer customer = customerRepository.findById(transactionDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setTransactionType(transactionDto.getTransactionType());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setDate(transactionDto.getDate());
        transaction.setStatus(transactionDto.getStatus());
        transaction.setPaymentMethod(transactionDto.getPaymentMethod());
        transaction.setNotes(transactionDto.getNotes());

        // Update customer total due based on transaction type
        updateCustomerTotalDue(customer, transactionDto.getTransactionType(), transactionDto.getAmount());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDto(savedTransaction);
    }

    public TransactionDto updateTransaction(Long id, TransactionDto transactionDto) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Revert previous amount from customer total due
        updateCustomerTotalDue(transaction.getCustomer(), 
                getOppositeTransactionType(transaction.getTransactionType()), 
                transaction.getAmount());

        transaction.setTransactionType(transactionDto.getTransactionType());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setDate(transactionDto.getDate());
        transaction.setStatus(transactionDto.getStatus());
        transaction.setPaymentMethod(transactionDto.getPaymentMethod());
        transaction.setNotes(transactionDto.getNotes());

        // Update customer total due with new amount
        updateCustomerTotalDue(transaction.getCustomer(), transactionDto.getTransactionType(), transactionDto.getAmount());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDto(savedTransaction);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Revert amount from customer total due
        updateCustomerTotalDue(transaction.getCustomer(), 
                getOppositeTransactionType(transaction.getTransactionType()), 
                transaction.getAmount());

        transactionRepository.deleteById(id);
    }

    public TransactionDto updateTransactionStatus(Long id, TransactionStatus status) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        TransactionStatus oldStatus = transaction.getStatus();
        transaction.setStatus(status);

        // Update customer total due if status changes
        if (oldStatus == TransactionStatus.PENDING && status == TransactionStatus.COMPLETED &&
                transaction.getTransactionType() == TransactionType.CREDIT) {
            // Credit transaction completed, no change to total due
        } else if (oldStatus == TransactionStatus.PENDING && status == TransactionStatus.CANCELLED) {
            // Transaction cancelled, revert amount from customer total due
            updateCustomerTotalDue(transaction.getCustomer(), 
                    getOppositeTransactionType(transaction.getTransactionType()), 
                    transaction.getAmount());
        }

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDto(savedTransaction);
    }

    public List<TransactionDto> getTransactionsByCustomer(String customerId) {
        return transactionRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getPendingTransactions() {
        return transactionRepository.findByStatus(TransactionStatus.PENDING).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getOverdueTransactions() {
        return transactionRepository.findByStatusAndDateBefore(TransactionStatus.PENDING, LocalDate.now())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BigDecimal getDailySales(LocalDate date) {
        return transactionRepository.findByTransactionTypeAndDateAndStatus(
                TransactionType.CREDIT, date, TransactionStatus.COMPLETED)
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getDailyCashReceived(LocalDate date) {
        return transactionRepository.findByTransactionTypeAndDateAndStatus(
                TransactionType.PAYMENT, date, TransactionStatus.COMPLETED)
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getDailyCreditGiven(LocalDate date) {
        return transactionRepository.findByTransactionTypeAndDateAndStatus(
                TransactionType.CREDIT, date, TransactionStatus.PENDING)
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getPeriodSales(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByTransactionTypeAndDateBetweenAndStatus(
                TransactionType.CREDIT, startDate, endDate, TransactionStatus.COMPLETED)
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getPeriodCashReceived(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByTransactionTypeAndDateBetweenAndStatus(
                TransactionType.PAYMENT, startDate, endDate, TransactionStatus.COMPLETED)
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getPeriodCreditGiven(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByTransactionTypeAndDateBetweenAndStatus(
                TransactionType.CREDIT, startDate, endDate, TransactionStatus.PENDING)
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void updateCustomerTotalDue(Customer customer, TransactionType transactionType, BigDecimal amount) {
        BigDecimal currentTotalDue = customer.getTotalDue();
        
        switch (transactionType) {
            case CREDIT:
                customer.setTotalDue(currentTotalDue.add(amount));
                break;
            case PAYMENT:
                customer.setTotalDue(currentTotalDue.subtract(amount));
                break;
            case ADJUSTMENT:
                // Adjustments can be positive or negative based on amount
                customer.setTotalDue(currentTotalDue.add(amount));
                break;
        }
        
        customer.setLastTransactionDate(LocalDate.now());
        customerRepository.save(customer);
    }

    private TransactionType getOppositeTransactionType(TransactionType type) {
        switch (type) {
            case CREDIT:
                return TransactionType.PAYMENT;
            case PAYMENT:
                return TransactionType.CREDIT;
            case ADJUSTMENT:
                return TransactionType.ADJUSTMENT; // No opposite for adjustment
            default:
                return TransactionType.CREDIT;
        }
    }

    private TransactionDto convertToDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setCustomerId(transaction.getCustomer().getId());
        dto.setCustomerName(transaction.getCustomer().getName());
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