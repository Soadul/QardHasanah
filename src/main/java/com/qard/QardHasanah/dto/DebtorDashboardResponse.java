package com.qard.QardHasanah.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Debtor dashboard data")
public class DebtorDashboardResponse {

    @Schema(description = "Debtor user info")
    private UserResponse user;

    @Schema(description = "Total debt amount")
    private BigDecimal totalDebt;

    @Schema(description = "Total amount paid")
    private BigDecimal totalPaid;

    @Schema(description = "Remaining balance to pay")
    private BigDecimal remainingBalance;

    @Schema(description = "Next EMI amount")
    private BigDecimal nextEmiAmount;

    @Schema(description = "Number of active loans")
    private Long activeLoans;

    @Schema(description = "All loans")
    private List<LoanResponse> loans;

    @Schema(description = "Recent payments")
    private List<PaymentResponse> recentPayments;
}

