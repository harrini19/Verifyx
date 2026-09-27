package com.certificate.verification.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a budget allocation for an organizational unit.
 * Used in the Unit Budget Report to track allocated vs. actual spending.
 */
@Entity
@Table(name = "budgets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

    @Column(nullable = false, length = 150)
    private String unitName;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal allocatedBudget;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal actualSpending;

    @Column(nullable = false)
    private Integer fiscalYear;

    @Column(length = 300)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.actualSpending == null) {
            this.actualSpending = BigDecimal.ZERO;
        }
    }

    /**
     * Returns remaining budget (allocated minus actual spending).
     */
    public BigDecimal getRemainingBudget() {
        return allocatedBudget.subtract(actualSpending);
    }

    /**
     * Returns utilization percentage (actual / allocated * 100).
     * Returns zero if allocated budget is zero to prevent division-by-zero.
     */
    public BigDecimal getUtilizationPercentage() {
        if (allocatedBudget == null || allocatedBudget.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return actualSpending
                .divide(allocatedBudget, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
