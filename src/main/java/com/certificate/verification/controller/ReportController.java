package com.certificate.verification.controller;

import com.certificate.verification.model.Budget;
import com.certificate.verification.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controller for financial reports: P&L and Unit Budget.
 */
@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/reports")
    public String reportsIndex() {
        return "redirect:/reports/pnl";
    }

    @GetMapping("/reports/pnl")
    public String pnlReport(Model model) {
        model.addAttribute("pnl", reportService.generatePnlReport());
        return "reports/pnl";
    }

    @GetMapping("/reports/budget")
    public String budgetReport(Model model) {
        model.addAttribute("budgets", reportService.generateBudgetReport());
        return "reports/budget";
    }

    // REST API endpoints
    @GetMapping("/api/reports/pnl")
    @ResponseBody
    public ResponseEntity<ReportService.PnlReport> apiPnlReport() {
        return ResponseEntity.ok(reportService.generatePnlReport());
    }

    @GetMapping("/api/reports/budget")
    @ResponseBody
    public ResponseEntity<List<Budget>> apiBudgetReport() {
        return ResponseEntity.ok(reportService.generateBudgetReport());
    }
}
