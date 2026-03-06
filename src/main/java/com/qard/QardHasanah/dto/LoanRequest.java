package com.qard.QardHasanah.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a loan")
public class LoanRequest {

    @NotNull(message = "Debtor ID is required")
    @Schema(description = "ID of the debtor (user taking the loan)", example = "5")
    private Long debtorId;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    @Schema(description = "Total loan amount", example = "10000.00")
    private BigDecimal totalAmount;

    @Positive(message = "Duration must be positive")
    @Schema(description = "Loan duration in months", example = "12")
    private Integer durationMonths;

    @Schema(description = "Reason for the loan", example = "Medical emergency")
    private String reason;
}

