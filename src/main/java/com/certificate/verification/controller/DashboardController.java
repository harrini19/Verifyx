package com.certificate.verification.controller;

import com.certificate.verification.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the main dashboard page.
 */
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /** Cinematic landing page — served at the root URL. */
    @GetMapping("/")
    public String landingPage(Model model) {
        DashboardService.DashboardStats stats = dashboardService.getDashboardStats();
        model.addAttribute("stats", stats);
        return "index";
    }

    /** Operational dashboard — served at /dashboard. */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        DashboardService.DashboardStats stats = dashboardService.getDashboardStats();
        model.addAttribute("stats", stats);
        return "dashboard";
    }
}
