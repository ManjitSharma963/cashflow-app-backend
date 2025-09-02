package com.shop.controller;

import com.shop.service.CustomerService;
import com.shop.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3002"}, allowedHeaders = "*")
public class DashboardController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerService customerService;

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping("/today")
    public ResponseEntity<Map<String, Object>> getTodayStats() {
        String userEmail = getCurrentUserEmail();
        LocalDate today = LocalDate.now();

        Map<String, Object> stats = new HashMap<>();
        stats.put("date", today);
        stats.put("dailySales", transactionService.getDailySales(userEmail, today));
        stats.put("dailyCashReceived", transactionService.getDailyCashReceived(userEmail, today));
        stats.put("dailyCreditGiven", transactionService.getDailyCreditGiven(userEmail, today));
        stats.put("totalOutstandingAmount", customerService.getTotalOutstandingBalance(userEmail));

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary(
            @RequestParam(defaultValue = "month") String period,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String userEmail = getCurrentUserEmail();
        Map<String, Object> summary = new HashMap<>();
        LocalDate now = LocalDate.now();
        LocalDate periodStartDate, periodEndDate;

        // Calculate period dates
        switch (period.toLowerCase()) {
            case "week":
                periodStartDate = now.minusWeeks(1);
                periodEndDate = now;
                break;
            case "month":
                periodStartDate = now.minusMonths(1);
                periodEndDate = now;
                break;
            case "year":
                periodStartDate = now.minusYears(1);
                periodEndDate = now;
                break;
            default:
                if (startDate != null && endDate != null) {
                    periodStartDate = startDate;
                    periodEndDate = endDate;
                } else {
                    periodStartDate = now.minusMonths(1);
                    periodEndDate = now;
                }
                break;
        }

        summary.put("period", period);
        summary.put("startDate", periodStartDate);
        summary.put("endDate", periodEndDate);
        summary.put("periodSales", transactionService.getPeriodSales(userEmail, periodStartDate, periodEndDate));
        summary.put("periodCashReceived", transactionService.getPeriodCashReceived(userEmail, periodStartDate, periodEndDate));
        summary.put("periodCreditGiven", transactionService.getPeriodCreditGiven(userEmail, periodStartDate, periodEndDate));
        summary.put("totalOutstandingAmount", customerService.getTotalOutstandingBalance(userEmail));

        return ResponseEntity.ok(summary);
    }
}