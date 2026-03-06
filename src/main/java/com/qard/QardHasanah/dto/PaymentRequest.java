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
@Schema(description = "Request to make a payment")
public class PaymentRequest {

    @NotNull(message = "Loan ID is required")
    @Schema(description = "ID of the loan", example = "1")
    private Long loanId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Schema(description = "Payment amount", example = "1000.00")
    private BigDecimal amount;

    @Schema(description = "Description of the payment", example = "Monthly EMI payment")
    private String description;
}

