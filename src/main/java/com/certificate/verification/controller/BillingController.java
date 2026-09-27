package com.certificate.verification.controller;

import com.certificate.verification.dto.InvoiceRequest;
import com.certificate.verification.dto.PaymentRequest;
import com.certificate.verification.model.Invoice;
import com.certificate.verification.model.PackageName;
import com.certificate.verification.model.Payment;
import com.certificate.verification.model.PaymentMethod;
import com.certificate.verification.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for API billing: invoice management and payment processing.
 */
@Controller
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    // ======================== UI ENDPOINTS ========================

    @GetMapping("/billing")
    public String billingDashboard(Model model) {
        model.addAttribute("invoices", billingService.getAllInvoices());
        model.addAttribute("payments", billingService.getAllPayments());
        return "billing/dashboard";
    }

    @GetMapping("/billing/invoices")
    public String listInvoices(Model model) {
        model.addAttribute("invoices", billingService.getAllInvoices());
        return "billing/invoices";
    }

    @GetMapping("/billing/invoices/new")
    public String showCreateInvoiceForm(Model model) {
        model.addAttribute("invoiceRequest", new InvoiceRequest());
        model.addAttribute("packages", PackageName.values());
        return "billing/invoice-form";
    }

    @PostMapping("/billing/invoices/new")
    public String createInvoice(@Valid @ModelAttribute("invoiceRequest") InvoiceRequest request,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("packages", PackageName.values());
            return "billing/invoice-form";
        }
        try {
            Invoice invoice = billingService.createInvoice(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Invoice " + invoice.getInvoiceNumber() + " created successfully.");
            return "redirect:/billing/invoices";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("packages", PackageName.values());
            return "billing/invoice-form";
        }
    }

    @GetMapping("/billing/payments")
    public String listPayments(Model model) {
        model.addAttribute("payments", billingService.getAllPayments());
        return "billing/payments";
    }

    @GetMapping("/billing/payments/new")
    public String showRegisterPaymentForm(Model model) {
        model.addAttribute("paymentRequest", new PaymentRequest());
        model.addAttribute("invoices", billingService.getAllInvoices());
        model.addAttribute("paymentMethods", PaymentMethod.values());
        return "billing/payment-form";
    }

    @PostMapping("/billing/payments/new")
    public String registerPayment(@Valid @ModelAttribute("paymentRequest") PaymentRequest request,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("invoices", billingService.getAllInvoices());
            model.addAttribute("paymentMethods", PaymentMethod.values());
            return "billing/payment-form";
        }
        try {
            Payment payment = billingService.registerPayment(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Payment " + payment.getPaymentReference() + " registered successfully.");
            return "redirect:/billing/payments";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("invoices", billingService.getAllInvoices());
            model.addAttribute("paymentMethods", PaymentMethod.values());
            return "billing/payment-form";
        }
    }

    // ======================== REST API ENDPOINTS ========================

    @PostMapping("/api/invoices")
    @ResponseBody
    public ResponseEntity<Invoice> apiCreateInvoice(@Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(billingService.createInvoice(request));
    }

    @GetMapping("/api/invoices")
    @ResponseBody
    public ResponseEntity<List<Invoice>> apiGetAllInvoices() {
        return ResponseEntity.ok(billingService.getAllInvoices());
    }

    @GetMapping("/api/invoices/{id}")
    @ResponseBody
    public ResponseEntity<Invoice> apiGetInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.getInvoiceById(id));
    }

    @PostMapping("/api/payments")
    @ResponseBody
    public ResponseEntity<Payment> apiRegisterPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(billingService.registerPayment(request));
    }

    @GetMapping("/api/payments")
    @ResponseBody
    public ResponseEntity<List<Payment>> apiGetAllPayments() {
        return ResponseEntity.ok(billingService.getAllPayments());
    }
}
