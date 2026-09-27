package com.certificate.verification.controller;

import com.certificate.verification.dto.AccountRequest;
import com.certificate.verification.model.Account;
import com.certificate.verification.model.AccountType;
import com.certificate.verification.service.AccountService;
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
 * Controller for Chart of Accounts management.
 * Handles both Thymeleaf UI and REST API endpoints.
 */
@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // ======================== UI ENDPOINTS ========================

    @GetMapping("/accounts")
    public String listAccounts(@RequestParam(required = false) String search, Model model) {
        List<Account> accounts = (search != null && !search.isBlank())
                ? accountService.searchAccounts(search)
                : accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        model.addAttribute("search", search);
        return "accounts/list";
    }

    @GetMapping("/accounts/new")
    public String showCreateForm(Model model) {
        model.addAttribute("accountRequest", new AccountRequest());
        model.addAttribute("accountTypes", AccountType.values());
        return "accounts/form";
    }

    @PostMapping("/accounts/new")
    public String createAccount(@Valid @ModelAttribute("accountRequest") AccountRequest request,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("accountTypes", AccountType.values());
            return "accounts/form";
        }
        try {
            accountService.createAccount(request);
            redirectAttributes.addFlashAttribute("successMessage", "Account created successfully.");
            return "redirect:/accounts";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("accountTypes", AccountType.values());
            return "accounts/form";
        }
    }

    @GetMapping("/accounts/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Account account = accountService.getAccountById(id);
        AccountRequest req = AccountRequest.builder()
                .accountCode(account.getAccountCode())
                .accountName(account.getAccountName())
                .accountType(account.getAccountType())
                .description(account.getDescription())
                .openingBalance(account.getOpeningBalance())
                .build();
        model.addAttribute("accountRequest", req);
        model.addAttribute("accountId", id);
        model.addAttribute("accountTypes", AccountType.values());
        return "accounts/form";
    }

    @PostMapping("/accounts/{id}/edit")
    public String updateAccount(@PathVariable Long id,
                                @Valid @ModelAttribute("accountRequest") AccountRequest request,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("accountId", id);
            model.addAttribute("accountTypes", AccountType.values());
            return "accounts/form";
        }
        try {
            accountService.updateAccount(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Account updated successfully.");
            return "redirect:/accounts";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("accountId", id);
            model.addAttribute("accountTypes", AccountType.values());
            return "accounts/form";
        }
    }

    @PostMapping("/accounts/{id}/toggle")
    public String toggleAccountStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Account updated = accountService.toggleStatus(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "Account " + updated.getAccountCode() + " is now " + updated.getStatus().name());
        return "redirect:/accounts";
    }

    // ======================== REST API ENDPOINTS ========================

    @PostMapping("/api/accounts")
    @ResponseBody
    public ResponseEntity<Account> apiCreateAccount(@Valid @RequestBody AccountRequest request) {
        Account account = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/api/accounts")
    @ResponseBody
    public ResponseEntity<List<Account>> apiGetAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PutMapping("/api/accounts/{id}")
    @ResponseBody
    public ResponseEntity<Account> apiUpdateAccount(@PathVariable Long id,
                                                    @Valid @RequestBody AccountRequest request) {
        return ResponseEntity.ok(accountService.updateAccount(id, request));
    }
}
