package com.certificate.verification.controller;

import com.certificate.verification.dto.CertificateRequest;
import com.certificate.verification.dto.CertificateResponse;
import com.certificate.verification.dto.VerificationResponse;
import com.certificate.verification.service.CertificateService;
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
 * Controller for certificate issuance, listing, and verification.
 * Handles both UI (Thymeleaf) and REST API endpoints.
 */
@Controller
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    // ======================== UI ENDPOINTS ========================

    @GetMapping("/certificates")
    public String listCertificates(Model model) {
        model.addAttribute("certificates", certificateService.getAllCertificates());
        return "certificates/list";
    }

    @GetMapping("/certificates/issue")
    public String showIssueForm(Model model) {
        model.addAttribute("certificateRequest", new CertificateRequest());
        return "certificates/issue";
    }

    @PostMapping("/certificates/issue")
    public String issueCertificate(@Valid @ModelAttribute("certificateRequest") CertificateRequest request,
                                   BindingResult bindingResult,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "certificates/issue";
        }
        try {
            CertificateResponse response = certificateService.issueCertificate(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Certificate issued successfully! SHA-256 Hash: " + response.getSha256Hash());
            redirectAttributes.addFlashAttribute("issuedCertificate", response);
            return "redirect:/certificates/issued";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "certificates/issue";
        }
    }

    @GetMapping("/certificates/issued")
    public String showIssuedCertificate(Model model) {
        return "certificates/issued";
    }

    @GetMapping("/certificates/verify")
    public String showVerifyPage(Model model) {
        return "certificates/verify";
    }

    @PostMapping("/certificates/verify")
    public String verifyFromForm(@RequestParam("hash") String hash, Model model) {
        VerificationResponse response = certificateService.verifyCertificate(hash);
        model.addAttribute("verificationResult", response);
        model.addAttribute("searchedHash", hash);
        return "certificates/verify";
    }

    // ======================== REST API ENDPOINTS ========================

    @PostMapping("/api/certificates")
    @ResponseBody
    public ResponseEntity<CertificateResponse> apiIssueCertificate(
            @Valid @RequestBody CertificateRequest request) {
        CertificateResponse response = certificateService.issueCertificate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/certificates")
    @ResponseBody
    public ResponseEntity<List<CertificateResponse>> apiGetAllCertificates() {
        return ResponseEntity.ok(certificateService.getAllCertificates());
    }

    @GetMapping("/api/certificates/{id}")
    @ResponseBody
    public ResponseEntity<CertificateResponse> apiGetCertificateById(@PathVariable Long id) {
        return ResponseEntity.ok(certificateService.getCertificateById(id));
    }

    @GetMapping("/api/certificates/verify/{hash}")
    @ResponseBody
    public ResponseEntity<VerificationResponse> apiVerifyCertificate(@PathVariable String hash) {
        VerificationResponse response = certificateService.verifyCertificate(hash);
        HttpStatus status = response.isVerified() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }
}
