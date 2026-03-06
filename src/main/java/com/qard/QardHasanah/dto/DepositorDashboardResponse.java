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
@Schema(description = "Depositor dashboard data")
public class DepositorDashboardResponse {

    @Schema(description = "Depositor user info")
    private UserResponse user;

    @Schema(description = "Total deposited amount")
    private BigDecimal totalDeposited;

    @Schema(description = "Total amount lent to debtors")
    private BigDecimal totalLent;

    @Schema(description = "Available balance (deposited - lent)")
    private BigDecimal availableBalance;

    @Schema(description = "Number of active borrowers")
    private Long activeBorrowers;

    @Schema(description = "Recent deposits")
    private List<DepositResponse> recentDeposits;

    @Schema(description = "Active loans given to debtors")
    private List<LoanResponse> activeLoans;
}

