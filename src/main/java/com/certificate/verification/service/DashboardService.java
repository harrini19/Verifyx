package com.certificate.verification.service;

import com.certificate.verification.model.Certificate;
import com.certificate.verification.model.Invoice;
import com.certificate.verification.model.Payment;
import com.certificate.verification.repository.AccountRepository;
import com.certificate.verification.repository.InvoiceRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service for aggregating dashboard summary statistics.
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CertificateService certificateService;
    private final BillingService billingService;
    private final ReportService reportService;
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public DashboardStats getDashboardStats() {
        long totalCertificates = certificateService.countTotal();
        long verifiedCertificates = certificateService.countValid();
        long pendingInvoices = billingService.countPendingInvoices();

        ReportService.PnlReport pnl = reportService.generatePnlReport();

        List<Certificate> recentCertificates = certificateService.getRecentCertificates();
        List<Invoice> recentInvoices = billingService.getRecentInvoices();
        List<Payment> recentPayments = billingService.getRecentPayments();

        return new DashboardStats(
                totalCertificates, verifiedCertificates, pendingInvoices,
                pnl.getTotalRevenue(), pnl.getTotalExpenses(), pnl.getNetProfitLoss(),
                recentCertificates, recentInvoices, recentPayments
        );
    }

    @Data
    public static class DashboardStats {
        private final long totalCertificates;
        private final long verifiedCertificates;
        private final long pendingInvoices;
        private final BigDecimal totalRevenue;
        private final BigDecimal totalExpenses;
        private final BigDecimal netProfitLoss;
        private final List<Certificate> recentCertificates;
        private final List<Invoice> recentInvoices;
        private final List<Payment> recentPayments;

        public boolean isProfitable() {
            return netProfitLoss.compareTo(BigDecimal.ZERO) >= 0;
        }
    }
}
