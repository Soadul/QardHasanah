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
@Schema(description = "Admin dashboard data")
public class AdminDashboardResponse {

    @Schema(description = "Total deposits in the system")
    private BigDecimal totalDeposits;

    @Schema(description = "Total loans given out")
    private BigDecimal totalLoans;

    @Schema(description = "Total amount recovered")
    private BigDecimal totalRecovered;

    @Schema(description = "Total outstanding debt")
    private BigDecimal totalOutstandingDebt;

    @Schema(description = "Available fund balance")
    private BigDecimal availableFunds;

    @Schema(description = "Total number of depositors")
    private Long totalDepositors;

    @Schema(description = "Total number of debtors")
    private Long totalDebtors;

    @Schema(description = "Number of active loans")
    private Long activeLoansCount;

    @Schema(description = "Number of completed loans")
    private Long completedLoansCount;

    @Schema(description = "List of all depositors")
    private List<UserResponse> depositors;

    @Schema(description = "List of all debtors")
    private List<UserResponse> debtors;

    @Schema(description = "Recent loans")
    private List<LoanResponse> recentLoans;

    @Schema(description = "Recent deposits")
    private List<DepositResponse> recentDeposits;
}

