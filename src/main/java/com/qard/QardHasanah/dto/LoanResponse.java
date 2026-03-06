package com.qard.QardHasanah.dto;

import com.qard.QardHasanah.entity.LoanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Loan response object")
public class LoanResponse {

    @Schema(description = "Loan ID")
    private Long id;

    @Schema(description = "Debtor ID")
    private Long debtorId;

    @Schema(description = "Debtor name")
    private String debtorName;

    @Schema(description = "Depositor ID")
    private Long depositorId;

    @Schema(description = "Depositor name")
    private String depositorName;

    @Schema(description = "Total loan amount")
    private BigDecimal totalAmount;

    @Schema(description = "Total amount paid")
    private BigDecimal totalPaid;

    @Schema(description = "Remaining balance")
    private BigDecimal remainingBalance;

    @Schema(description = "Monthly EMI amount")
    private BigDecimal monthlyEmi;

    @Schema(description = "Loan duration in months")
    private Integer durationMonths;

    @Schema(description = "Reason for loan")
    private String reason;

    @Schema(description = "Loan status")
    private LoanStatus status;

    @Schema(description = "Loan date timestamp")
    private Long loanDate;

    @Schema(description = "Created at timestamp")
    private Long createdAt;
}

