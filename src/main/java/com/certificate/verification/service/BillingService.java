package com.certificate.verification.service;

import com.certificate.verification.dto.InvoiceRequest;
import com.certificate.verification.dto.PaymentRequest;
import com.certificate.verification.exception.DuplicateResourceException;
import com.certificate.verification.exception.ResourceNotFoundException;
import com.certificate.verification.model.*;
import com.certificate.verification.repository.AccountRepository;
import com.certificate.verification.repository.InvoiceRepository;
import com.certificate.verification.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Service layer for API billing — invoice creation and simulated payment processing.
 *
 * ACADEMIC NOTE: This is a simulated billing module for demonstration purposes.
 * No real financial transactions are processed.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;

    // Package pricing (amount and query limit)
    private static final BigDecimal BASIC_PRICE = new BigDecimal("1000.00");
    private static final BigDecimal STANDARD_PRICE = new BigDecimal("4000.00");
    private static final BigDecimal PREMIUM_PRICE = new BigDecimal("7000.00");

    private static final int BASIC_QUERIES = 1000;
    private static final int STANDARD_QUERIES = 5000;
    private static final int PREMIUM_QUERIES = 10000;

    @Transactional
    public Invoice createInvoice(InvoiceRequest request) {
        String invoiceNumber = generateInvoiceNumber();

        BigDecimal amount = getPackagePrice(request.getPackageName());
        int queryLimit = getPackageQueryLimit(request.getPackageName());

        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .agencyName(request.getAgencyName())
                .packageName(request.getPackageName())
                .queryLimit(queryLimit)
                .amount(amount)
                .invoiceDate(request.getInvoiceDate())
                .dueDate(request.getDueDate())
                .status(InvoiceStatus.PENDING)
                .build();

        Invoice saved = invoiceRepository.save(invoice);
        log.info("Invoice created: {} for agency: {}", invoiceNumber, request.getAgencyName());
        return saved;
    }

    /**
     * Registers a simulated payment for an invoice.
     * Steps:
     * 1. Validate the invoice exists and is not already paid/cancelled.
     * 2. Validate payment amount matches invoice amount.
     * 3. Mark invoice as PAID.
     * 4. Record the payment.
     * 5. Credit the API Revenue account balance.
     */
    @Transactional
    public Payment registerPayment(PaymentRequest request) {
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", request.getInvoiceId()));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalArgumentException("Invoice " + invoice.getInvoiceNumber() + " is already paid.");
        }
        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new IllegalArgumentException("Invoice " + invoice.getInvoiceNumber() + " has been cancelled.");
        }
        if (request.getAmount().compareTo(invoice.getAmount()) != 0) {
            throw new IllegalArgumentException(
                    "Payment amount ₹" + request.getAmount() +
                    " does not match invoice amount ₹" + invoice.getAmount());
        }

        // Mark invoice paid
        invoice.setStatus(InvoiceStatus.PAID);
        invoiceRepository.save(invoice);

        // Generate payment reference
        String paymentRef = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Payment payment = Payment.builder()
                .paymentReference(paymentRef)
                .invoiceId(invoice.getInvoiceId())
                .amount(request.getAmount())
                .paymentDate(request.getPaymentDate())
                .paymentMethod(request.getPaymentMethod())
                .bankAccount(request.getBankAccount())
                .status("COMPLETED")
                .build();

        Payment saved = paymentRepository.save(payment);
        log.info("Payment registered: {} for invoice: {}", paymentRef, invoice.getInvoiceNumber());

        // Update API Revenue account balance (account code 4002)
        accountRepository.findByAccountCode("4002").ifPresent(account -> {
            account.setCurrentBalance(account.getCurrentBalance().add(request.getAmount()));
            accountRepository.save(account);
        });

        return saved;
    }

    @Transactional(readOnly = true)
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", id));
    }

    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public long countPendingInvoices() {
        return invoiceRepository.countByStatus(InvoiceStatus.PENDING);
    }

    public BigDecimal getTotalApiRevenue() {
        return invoiceRepository.sumPaidInvoiceAmounts();
    }

    public List<Invoice> getRecentInvoices() {
        return invoiceRepository.findTop5ByOrderByCreatedAtDesc();
    }

    public List<Payment> getRecentPayments() {
        return paymentRepository.findTop5ByOrderByCreatedAtDesc();
    }

    private BigDecimal getPackagePrice(PackageName packageName) {
        return switch (packageName) {
            case BASIC -> BASIC_PRICE;
            case STANDARD -> STANDARD_PRICE;
            case PREMIUM -> PREMIUM_PRICE;
        };
    }

    private int getPackageQueryLimit(PackageName packageName) {
        return switch (packageName) {
            case BASIC -> BASIC_QUERIES;
            case STANDARD -> STANDARD_QUERIES;
            case PREMIUM -> PREMIUM_QUERIES;
        };
    }

    private String generateInvoiceNumber() {
        String year = String.valueOf(LocalDate.now().getYear());
        long count = invoiceRepository.count() + 1;
        return String.format("INV-%s-%04d", year, count);
    }
}
