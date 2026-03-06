package com.qard.QardHasanah.controller;

import com.qard.QardHasanah.dto.*;
import com.qard.QardHasanah.security.JwtUtil;
import com.qard.QardHasanah.service.DepositService;
import com.qard.QardHasanah.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/depositor")
@CrossOrigin(origins = "*")
@Tag(name = "Depositor", description = "APIs for depositors to manage deposits and loans")
@SecurityRequirement(name = "bearerAuth")
public class DepositorController {

    @Autowired
    private DepositService depositService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Get depositor dashboard
     */
    @GetMapping("/dashboard")
    @Operation(summary = "Get depositor dashboard", description = "Get dashboard with deposit summary, borrowers, and loan info")
    @PreAuthorize("hasRole('DEPOSITOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<DepositorDashboardResponse>> getDashboard(
            @RequestHeader("Authorization") String authHeader) {
        Long depositorId = extractUserIdFromToken(authHeader);
        DepositorDashboardResponse dashboard = depositService.getDepositorDashboard(depositorId);
        return ResponseEntity.ok(new ApiResponse<>("Dashboard retrieved successfully", dashboard, true, HttpStatus.OK.value()));
    }

    /**
     * Make a deposit
     */
    @PostMapping("/deposit")
    @Operation(summary = "Make a deposit", description = "Depositor adds money to the fund")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Deposit created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PreAuthorize("hasRole('DEPOSITOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<DepositResponse>> makeDeposit(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody DepositRequest request) {
        Long depositorId = extractUserIdFromToken(authHeader);
        DepositResponse deposit = depositService.createDeposit(depositorId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Deposit created successfully", deposit, true, HttpStatus.CREATED.value()));
    }

    /**
     * Get my deposits
     */
    @GetMapping("/my-deposits")
    @Operation(summary = "Get my deposits", description = "Get all deposits made by the logged-in depositor")
    @PreAuthorize("hasRole('DEPOSITOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<DepositResponse>>> getMyDeposits(
            @RequestHeader("Authorization") String authHeader) {
        Long depositorId = extractUserIdFromToken(authHeader);
        List<DepositResponse> deposits = depositService.getDepositsByDepositorId(depositorId);
        return ResponseEntity.ok(new ApiResponse<>("Deposits retrieved successfully", deposits, true, HttpStatus.OK.value()));
    }

    /**
     * Create a loan for a debtor
     */
    @PostMapping("/loan")
    @Operation(summary = "Create a loan", description = "Depositor creates a loan for a debtor")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Loan created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PreAuthorize("hasRole('DEPOSITOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<LoanResponse>> createLoan(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody LoanRequest request) {
        Long depositorId = extractUserIdFromToken(authHeader);
        LoanResponse loan = loanService.createLoan(depositorId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Loan created successfully", loan, true, HttpStatus.CREATED.value()));
    }

    /**
     * Get loans given by this depositor
     */
    @GetMapping("/my-borrowers")
    @Operation(summary = "Get my borrowers", description = "Get all loans given to debtors by the logged-in depositor")
    @PreAuthorize("hasRole('DEPOSITOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getMyBorrowers(
            @RequestHeader("Authorization") String authHeader) {
        Long depositorId = extractUserIdFromToken(authHeader);
        List<LoanResponse> loans = loanService.getLoansByDepositorId(depositorId);
        return ResponseEntity.ok(new ApiResponse<>("Borrowers retrieved successfully", loans, true, HttpStatus.OK.value()));
    }

    private Long extractUserIdFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.extractUserId(token);
    }
}

