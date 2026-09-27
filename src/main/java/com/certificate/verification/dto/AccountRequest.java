package com.certificate.verification.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Request DTO for creating or updating a Chart of Accounts entry.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {

    @NotBlank(message = "Account code is required")
    @Size(max = 20, message = "Account code must not exceed 20 characters")
    private String accountCode;

    @NotBlank(message = "Account name is required")
    @Size(max = 150, message = "Account name must not exceed 150 characters")
    private String accountName;

    @NotNull(message = "Account type is required")
    private com.certificate.verification.model.AccountType accountType;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Opening balance is required")
    @DecimalMin(value = "0.00", message = "Opening balance cannot be negative")
    private BigDecimal openingBalance;
}
