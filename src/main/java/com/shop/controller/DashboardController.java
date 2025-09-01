package com.shop.controller;

import com.shop.service.CustomerService;
import com.shop.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DashboardController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/today")
    public ResponseEntity<Map<String, Object>> getTodayStats() {
        LocalDate today = LocalDate.now();

        Map<String, Object> stats = new HashMap<>();
        stats.put("date", today);
        stats.put("dailySales", transactionService.getDailySales(today));
        stats.put("dailyCashReceived", transactionService.getDailyCashReceived(today));
        stats.put("dailyCreditGiven", transactionService.getDailyCreditGiven(today));
        stats.put("totalOutstandingAmount", customerService.getTotalOutstandingBalance());

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary(
            @RequestParam(defaultValue = "month") String period,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

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
        summary.put("periodSales", transactionService.getPeriodSales(periodStartDate, periodEndDate));
        summary.put("periodCashReceived", transactionService.getPeriodCashReceived(periodStartDate, periodEndDate));
        summary.put("periodCreditGiven", transactionService.getPeriodCreditGiven(periodStartDate, periodEndDate));
        summary.put("totalOutstandingAmount", customerService.getTotalOutstandingBalance());

        return ResponseEntity.ok(summary);
    }
}