package com.certificate.verification.config;

import com.certificate.verification.dto.CertificateRequest;
import com.certificate.verification.model.*;
import com.certificate.verification.repository.*;
import com.certificate.verification.service.CertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Seeds the database with realistic sample data on first application startup.
 * Data will not be re-inserted if records already exist (idempotent).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final AccountRepository accountRepository;
    private final CertificateService certificateService;
    private final CertificateRepository certificateRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final BudgetRepository budgetRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        seedAccounts();
        seedCertificates();
        seedInvoicesAndPayments();
        seedBudgets();
        log.info("=== Data initialization complete ===");
    }

    private void seedAccounts() {
        if (accountRepository.count() > 0) return;

        log.info("Seeding Chart of Accounts...");

        accountRepository.save(Account.builder()
                .accountCode("1001")
                .accountName("Bank Account - Platform Operations")
                .accountType(AccountType.ASSET)
                .description("Primary bank account for platform financial operations")
                .openingBalance(new BigDecimal("500000.00"))
                .currentBalance(new BigDecimal("500000.00"))
                .status(AccountStatus.ACTIVE)
                .build());

        accountRepository.save(Account.builder()
                .accountCode("1101")
                .accountName("Accounts Receivable")
                .accountType(AccountType.ASSET)
                .description("Outstanding dues from agencies for API packages")
                .openingBalance(new BigDecimal("0.00"))
                .currentBalance(new BigDecimal("0.00"))
                .status(AccountStatus.ACTIVE)
                .build());

        accountRepository.save(Account.builder()
                .accountCode("2001")
                .accountName("Accounts Payable")
                .accountType(AccountType.LIABILITY)
                .description("Amounts owed to vendors and service providers")
                .openingBalance(new BigDecimal("0.00"))
                .currentBalance(new BigDecimal("0.00"))
                .status(AccountStatus.ACTIVE)
                .build());

        accountRepository.save(Account.builder()
                .accountCode("4001")
                .accountName("Certificate Issuance Revenue")
                .accountType(AccountType.REVENUE)
                .description("Revenue from digital certificate issuance fees")
                .openingBalance(new BigDecimal("25000.00"))
                .currentBalance(new BigDecimal("25000.00"))
                .status(AccountStatus.ACTIVE)
                .build());

        accountRepository.save(Account.builder()
                .accountCode("4002")
                .accountName("API Verification Revenue")
                .accountType(AccountType.REVENUE)
                .description("Revenue from agency API query packages")
                .openingBalance(new BigDecimal("11000.00"))
                .currentBalance(new BigDecimal("11000.00"))
                .status(AccountStatus.ACTIVE)
                .build());

        accountRepository.save(Account.builder()
                .accountCode("5001")
                .accountName("Platform Operating Expenses")
                .accountType(AccountType.EXPENSE)
                .description("Server hosting, maintenance, and operational costs")
                .openingBalance(new BigDecimal("18000.00"))
                .currentBalance(new BigDecimal("18000.00"))
                .status(AccountStatus.ACTIVE)
                .build());

        accountRepository.save(Account.builder()
                .accountCode("5002")
                .accountName("Staff & Administration Expenses")
                .accountType(AccountType.EXPENSE)
                .description("Salaries and administrative expenses")
                .openingBalance(new BigDecimal("30000.00"))
                .currentBalance(new BigDecimal("30000.00"))
                .status(AccountStatus.ACTIVE)
                .build());

        log.info("Seeded {} accounts", accountRepository.count());
    }

    private void seedCertificates() {
        if (certificateRepository.count() > 0) return;

        log.info("Seeding sample certificates...");

        issueSampleCertificate("CERT-2026-0001", "Arun Kumar", "STU2026001",
                "ABC Institute of Technology", "B.Tech Artificial Intelligence and Data Science",
                "A+", LocalDate.of(2026, 5, 15), "Registrar");

        issueSampleCertificate("CERT-2026-0002", "Priya Sharma", "STU2026002",
                "National Institute of Technology", "B.Tech Computer Science and Engineering",
                "A", LocalDate.of(2026, 5, 15), "Registrar");

        issueSampleCertificate("CERT-2026-0003", "Rahul Verma", "STU2026003",
                "ABC Institute of Technology", "M.Tech Data Science",
                "B+", LocalDate.of(2026, 6, 1), "Dean of Academics");

        issueSampleCertificate("CERT-2026-0004", "Sneha Patel", "STU2026004",
                "State University of Engineering", "B.Tech Electronics and Communication",
                "A", LocalDate.of(2026, 6, 10), "Registrar");

        issueSampleCertificate("CERT-2025-0100", "Vikram Singh", "STU2025100",
                "Central Institute of Management", "MBA Business Analytics",
                "A+", LocalDate.of(2025, 11, 20), "Director");

        log.info("Seeded {} certificates", certificateRepository.count());
    }

    private void issueSampleCertificate(String certNumber, String studentName, String studentId,
                                         String institution, String course, String grade,
                                         LocalDate issueDate, String issuer) {
        try {
            CertificateRequest req = CertificateRequest.builder()
                    .certificateNumber(certNumber)
                    .studentName(studentName)
                    .studentId(studentId)
                    .institution(institution)
                    .course(course)
                    .grade(grade)
                    .issueDate(issueDate)
                    .issuer(issuer)
                    .build();
            certificateService.issueCertificate(req);
        } catch (Exception e) {
            log.warn("Could not seed certificate {}: {}", certNumber, e.getMessage());
        }
    }

    private void seedInvoicesAndPayments() {
        if (invoiceRepository.count() > 0) return;

        log.info("Seeding invoices and payments...");

        Invoice inv1 = Invoice.builder()
                .invoiceNumber("INV-2026-0001")
                .agencyName("EduVerify Services Pvt. Ltd.")
                .packageName(PackageName.STANDARD)
                .queryLimit(5000)
                .amount(new BigDecimal("4000.00"))
                .invoiceDate(LocalDate.of(2026, 8, 1))
                .dueDate(LocalDate.of(2026, 8, 31))
                .status(InvoiceStatus.PAID)
                .build();
        inv1 = invoiceRepository.save(inv1);

        Invoice inv2 = Invoice.builder()
                .invoiceNumber("INV-2026-0002")
                .agencyName("CertCheck Analytics")
                .packageName(PackageName.PREMIUM)
                .queryLimit(10000)
                .amount(new BigDecimal("7000.00"))
                .invoiceDate(LocalDate.of(2026, 9, 1))
                .dueDate(LocalDate.of(2026, 9, 30))
                .status(InvoiceStatus.PAID)
                .build();
        inv2 = invoiceRepository.save(inv2);

        Invoice inv3 = Invoice.builder()
                .invoiceNumber("INV-2026-0003")
                .agencyName("GlobalHR Solutions")
                .packageName(PackageName.BASIC)
                .queryLimit(1000)
                .amount(new BigDecimal("1000.00"))
                .invoiceDate(LocalDate.of(2026, 9, 15))
                .dueDate(LocalDate.of(2026, 10, 15))
                .status(InvoiceStatus.PENDING)
                .build();
        invoiceRepository.save(inv3);

        // Register payments for paid invoices
        paymentRepository.save(Payment.builder()
                .paymentReference("PAY-A1B2C3D4")
                .invoiceId(inv1.getInvoiceId())
                .amount(new BigDecimal("4000.00"))
                .paymentDate(LocalDate.of(2026, 8, 10))
                .paymentMethod(PaymentMethod.BANK_TRANSFER)
                .bankAccount("HDFC-002-001")
                .status("COMPLETED")
                .build());

        paymentRepository.save(Payment.builder()
                .paymentReference("PAY-E5F6G7H8")
                .invoiceId(inv2.getInvoiceId())
                .amount(new BigDecimal("7000.00"))
                .paymentDate(LocalDate.of(2026, 9, 5))
                .paymentMethod(PaymentMethod.UPI)
                .bankAccount("UPI-certcheck@hdfc")
                .status("COMPLETED")
                .build());

        log.info("Seeded {} invoices and {} payments",
                invoiceRepository.count(), paymentRepository.count());
    }

    private void seedBudgets() {
        if (budgetRepository.count() > 0) return;

        log.info("Seeding budget records...");

        budgetRepository.save(Budget.builder()
                .unitName("Technology & Infrastructure")
                .allocatedBudget(new BigDecimal("200000.00"))
                .actualSpending(new BigDecimal("145000.00"))
                .fiscalYear(2026)
                .description("Server infrastructure, cloud services, and software licenses")
                .build());

        budgetRepository.save(Budget.builder()
                .unitName("Human Resources & Administration")
                .allocatedBudget(new BigDecimal("350000.00"))
                .actualSpending(new BigDecimal("312000.00"))
                .fiscalYear(2026)
                .description("Staff salaries, training, and administrative expenses")
                .build());

        budgetRepository.save(Budget.builder()
                .unitName("Marketing & Business Development")
                .allocatedBudget(new BigDecimal("80000.00"))
                .actualSpending(new BigDecimal("42000.00"))
                .fiscalYear(2026)
                .description("Marketing campaigns, partnerships, and agency onboarding")
                .build());

        budgetRepository.save(Budget.builder()
                .unitName("Research & Development")
                .allocatedBudget(new BigDecimal("120000.00"))
                .actualSpending(new BigDecimal("55000.00"))
                .fiscalYear(2026)
                .description("Platform feature development and security research")
                .build());

        budgetRepository.save(Budget.builder()
                .unitName("Customer Support")
                .allocatedBudget(new BigDecimal("60000.00"))
                .actualSpending(new BigDecimal("38000.00"))
                .fiscalYear(2026)
                .description("Support team operations and helpdesk infrastructure")
                .build());

        log.info("Seeded {} budget records", budgetRepository.count());
    }
}
