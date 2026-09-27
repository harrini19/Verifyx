package com.certificate.verification.dto;

import com.certificate.verification.model.PackageName;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for creating an API billing invoice.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRequest {

    @NotBlank(message = "Agency name is required")
    @Size(max = 200, message = "Agency name must not exceed 200 characters")
    private String agencyName;

    @NotNull(message = "Package name is required")
    private PackageName packageName;

    @NotNull(message = "Invoice date is required")
    private LocalDate invoiceDate;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
}
