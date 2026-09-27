package com.certificate.verification.service;

import com.certificate.verification.model.Account;
import com.certificate.verification.model.AccountType;
import com.certificate.verification.model.Budget;
import com.certificate.verification.repository.AccountRepository;
import com.certificate.verification.repository.BudgetRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service for generating financial reports:
 * - Platform P&L (Profit and Loss) Report
 * - Unit Budget Report
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final AccountRepository accountRepository;
    private final BudgetRepository budgetRepository;

    /**
     * Generates the Platform P&L Report.
     * Revenue accounts minus Expense accounts = Net Profit/Loss.
     */
    @Transactional(readOnly = true)
    public PnlReport generatePnlReport() {
        List<Account> accounts = accountRepository.findAll();

        BigDecimal totalRevenue = accounts.stream()
                .filter(a -> a.getAccountType() == AccountType.REVENUE)
                .map(Account::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = accounts.stream()
                .filter(a -> a.getAccountType() == AccountType.EXPENSE)
                .map(Account::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netProfitLoss = totalRevenue.subtract(totalExpenses);

        BigDecimal certRevenue = accounts.stream()
                .filter(a -> "4001".equals(a.getAccountCode()))
                .map(Account::getCurrentBalance)
                .findFirst()
                .orElse(BigDecimal.ZERO);

        BigDecimal apiRevenue = accounts.stream()
                .filter(a -> "4002".equals(a.getAccountCode()))
                .map(Account::getCurrentBalance)
                .findFirst()
                .orElse(BigDecimal.ZERO);

        return new PnlReport(totalRevenue, certRevenue, apiRevenue, totalExpenses, netProfitLoss);
    }

    /**
     * Generates the Unit Budget Report.
     * Shows allocated budget, actual spending, remaining budget, and utilization % per unit.
     */
    @Transactional(readOnly = true)
    public List<Budget> generateBudgetReport() {
        return budgetRepository.findAll();
    }

    /**
     * DTO for P&L report data.
     */
    @Data
    public static class PnlReport {
        private final BigDecimal totalRevenue;
        private final BigDecimal certificateIssuanceRevenue;
        private final BigDecimal apiVerificationRevenue;
        private final BigDecimal totalExpenses;
        private final BigDecimal netProfitLoss;

        public boolean isProfitable() {
            return netProfitLoss.compareTo(BigDecimal.ZERO) >= 0;
        }
    }
}
