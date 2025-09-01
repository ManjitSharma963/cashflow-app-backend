package com.shop.service;

import com.shop.dto.CustomerDto;
import com.shop.entity.Customer;
import com.shop.repository.CustomerRepository;
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
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CustomerDto getCustomerById(String id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(this::convertToDto).orElse(null);
    }

    public CustomerDto createCustomer(CustomerDto customerDto) {
        // Validate required fields for creation
        if (customerDto.getName() == null || customerDto.getName().trim().isEmpty()) {
            throw new RuntimeException("Customer name is required");
        }
        if (customerDto.getMobile() == null || customerDto.getMobile().trim().isEmpty()) {
            throw new RuntimeException("Customer mobile number is required");
        }

        // Check if mobile number already exists
        if (customerRepository.existsByMobile(customerDto.getMobile())) {
            throw new RuntimeException("Customer with mobile number " + customerDto.getMobile() + " already exists");
        }

        Customer customer = new Customer();
        customer.setId(customerDto.getMobile()); // Set ID to mobile number
        customer.setName(customerDto.getName());
        customer.setMobile(customerDto.getMobile());
        customer.setAddress(customerDto.getAddress());
        customer.setCategory(customerDto.getCategory() != null ? customerDto.getCategory() : "Regular");
        customer.setNotes(customerDto.getNotes());
        customer.setTotalDue(customerDto.getTotalDue() != null ? customerDto.getTotalDue() : BigDecimal.ZERO);
        customer.setLastTransactionDate(customerDto.getLastTransactionDate());
        customer.setIsActive(customerDto.getIsActive() != null ? customerDto.getIsActive() : true);

        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }

    public CustomerDto updateCustomer(String id, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Check if mobile number is being changed and if it already exists
        if (customerDto.getMobile() != null && !id.equals(customerDto.getMobile()) &&
                customerRepository.existsByMobile(customerDto.getMobile())) {
            throw new RuntimeException("Customer with mobile number " + customerDto.getMobile() + " already exists");
        }

        // Only update fields that are provided (partial update)
        if (customerDto.getName() != null) {
            customer.setName(customerDto.getName());
        }
        if (customerDto.getMobile() != null) {
            customer.setMobile(customerDto.getMobile());
        }
        if (customerDto.getAddress() != null) {
            customer.setAddress(customerDto.getAddress());
        }
        if (customerDto.getCategory() != null) {
            customer.setCategory(customerDto.getCategory());
        }
        if (customerDto.getNotes() != null) {
            customer.setNotes(customerDto.getNotes());
        }
        if (customerDto.getTotalDue() != null) {
            customer.setTotalDue(customerDto.getTotalDue());
        }
        if (customerDto.getLastTransactionDate() != null) {
            customer.setLastTransactionDate(customerDto.getLastTransactionDate());
        }
        if (customerDto.getIsActive() != null) {
            customer.setIsActive(customerDto.getIsActive());
        }

        // If mobile number changed, update the ID as well
        if (customerDto.getMobile() != null && !id.equals(customerDto.getMobile())) {
            customer.setId(customerDto.getMobile());
        }

        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }

    public void deleteCustomer(String id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Check if customer has outstanding transactions
        if (customer.getTotalDue() != null && customer.getTotalDue().compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("Cannot delete customer with outstanding balance: " + customer.getTotalDue());
        }

        customerRepository.deleteById(id);
    }

    public List<CustomerDto> searchCustomers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllCustomers();
        }
        return customerRepository.findByNameContainingIgnoreCaseOrMobileContaining(query.trim(), query.trim())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CustomerDto> getCustomersByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return getAllCustomers();
        }
        return customerRepository.findByCategory(category.trim())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CustomerDto> getActiveCustomers() {
        return customerRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CustomerDto> getCustomersWithOutstandingBalance() {
        return customerRepository.findByTotalDueGreaterThan(BigDecimal.ZERO)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalOutstandingBalance() {
        BigDecimal total = customerRepository.getTotalOutstandingBalance();
        return total != null ? total : BigDecimal.ZERO;
    }

    public CustomerDto updateCustomerBalance(String customerId, BigDecimal amountChange) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new RuntimeException("Customer ID cannot be null or empty");
        }
        if (amountChange == null) {
            throw new RuntimeException("Amount change cannot be null");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        BigDecimal currentBalance = customer.getTotalDue() != null ? customer.getTotalDue() : BigDecimal.ZERO;
        customer.setTotalDue(currentBalance.add(amountChange));
        customer.setLastTransactionDate(LocalDate.now());

        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }

    public CustomerDto updateCustomerTotalDue(String customerId, BigDecimal newTotalDue) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new RuntimeException("Customer ID cannot be null or empty");
        }
        if (newTotalDue == null) {
            throw new RuntimeException("Total due amount cannot be null");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setTotalDue(newTotalDue);
        customer.setLastTransactionDate(LocalDate.now());

        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }

    public CustomerDto patchCustomer(String customerId, CustomerDto customerDto) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new RuntimeException("Customer ID cannot be null or empty");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Partial update - only update non-null fields
        if (customerDto.getName() != null) {
            customer.setName(customerDto.getName());
        }
        if (customerDto.getMobile() != null) {
            customer.setMobile(customerDto.getMobile());
        }
        if (customerDto.getAddress() != null) {
            customer.setAddress(customerDto.getAddress());
        }
        if (customerDto.getCategory() != null) {
            customer.setCategory(customerDto.getCategory());
        }
        if (customerDto.getNotes() != null) {
            customer.setNotes(customerDto.getNotes());
        }
        if (customerDto.getTotalDue() != null) {
            customer.setTotalDue(customerDto.getTotalDue());
        }
        if (customerDto.getLastTransactionDate() != null) {
            customer.setLastTransactionDate(customerDto.getLastTransactionDate());
        }
        if (customerDto.getIsActive() != null) {
            customer.setIsActive(customerDto.getIsActive());
        }

        customer.setLastTransactionDate(LocalDate.now()); // Always update last transaction date
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }

    public List<CustomerDto> getCustomersByOutstandingBalanceRange(BigDecimal minAmount, BigDecimal maxAmount) {
        if (minAmount == null)
            minAmount = BigDecimal.ZERO;
        if (maxAmount == null)
            maxAmount = new BigDecimal("999999999.99");

        return customerRepository.findByTotalDueBetween(minAmount, maxAmount)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CustomerDto> getCustomersByLastTransactionDate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new RuntimeException("Start date and end date cannot be null");
        }

        return customerRepository.findByLastTransactionDateBetween(startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CustomerDto convertToDto(Customer customer) {
        if (customer == null) {
            return null;
        }

        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setMobile(customer.getMobile());
        dto.setAddress(customer.getAddress());
        dto.setCategory(customer.getCategory());
        dto.setNotes(customer.getNotes());
        dto.setTotalDue(customer.getTotalDue() != null ? customer.getTotalDue() : BigDecimal.ZERO);
        dto.setLastTransactionDate(customer.getLastTransactionDate());
        dto.setIsActive(customer.getIsActive() != null ? customer.getIsActive() : true);
        return dto;
    }
}