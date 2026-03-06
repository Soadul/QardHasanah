package com.qard.QardHasanah.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Deposit response object")
public class DepositResponse {

    @Schema(description = "Deposit ID")
    private Long id;

    @Schema(description = "Depositor ID")
    private Long depositorId;

    @Schema(description = "Depositor name")
    private String depositorName;

    @Schema(description = "Deposit amount")
    private BigDecimal amount;

    @Schema(description = "Description")
    private String description;

    @Schema(description = "Deposit date timestamp")
    private Long depositDate;

    @Schema(description = "Created at timestamp")
    private Long createdAt;
}

