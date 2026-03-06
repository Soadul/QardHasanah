package com.qard.QardHasanah.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Debtor ID is required")
    @Column(name = "debtor_id", nullable = false)
    private Long debtorId;

    @NotNull(message = "Depositor ID is required")
    @Column(name = "depositor_id", nullable = false)
    private Long depositorId;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "total_paid", precision = 15, scale = 2)
    private BigDecimal totalPaid = BigDecimal.ZERO;

    @Column(name = "monthly_emi", precision = 15, scale = 2)
    private BigDecimal monthlyEmi;

    @Column(name = "duration_months")
    private Integer durationMonths;

    @Column(length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status = LoanStatus.ACTIVE;

    @Column(name = "loan_date", nullable = false)
    private Long loanDate;

    @Column(name = "created_at")
    private Long createdAt = System.currentTimeMillis();

    @Column(name = "updated_at")
    private Long updatedAt = System.currentTimeMillis();
}

