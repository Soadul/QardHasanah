package com.qard.QardHasanah.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payment response object")
public class PaymentResponse {

    @Schema(description = "Payment ID")
    private Long id;

    @Schema(description = "Loan ID")
    private Long loanId;

    @Schema(description = "Payment amount")
    private BigDecimal amount;

    @Schema(description = "Description")
    private String description;

    @Schema(description = "Payment date timestamp")
    private Long paymentDate;

    @Schema(description = "Created at timestamp")
    private Long createdAt;
}

