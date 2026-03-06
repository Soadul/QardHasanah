package com.qard.QardHasanah.controller;
import com.qard.QardHasanah.dto.*;
import com.qard.QardHasanah.entity.LoanStatus;
import com.qard.QardHasanah.entity.Role;
import com.qard.QardHasanah.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@Tag(name = "Admin", description = "APIs for Super Admin to manage the entire system")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminController {
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private DepositService depositService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserService userService;
    @GetMapping("/dashboard")
    @Operation(summary = "Get admin dashboard", description = "Get complete dashboard with all system statistics")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard() {
        AdminDashboardResponse dashboard = dashboardService.getAdminDashboard();
        return ResponseEntity.ok(new ApiResponse<>("Dashboard retrieved successfully", dashboard, true, HttpStatus.OK.value()));
    }
    @GetMapping("/depositors")
    @Operation(summary = "Get all depositors", description = "Get list of all depositors in the system")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllDepositors() {
        List<UserResponse> depositors = userService.getUsersByRole(Role.DEPOSITOR);
        return ResponseEntity.ok(new ApiResponse<>("Depositors retrieved successfully", depositors, true, HttpStatus.OK.value()));
    }
    @GetMapping("/debtors")
    @Operation(summary = "Get all debtors", description = "Get list of all debtors in the system")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllDebtors() {
        List<UserResponse> debtors = userService.getUsersByRole(Role.DEBTOR);
        return ResponseEntity.ok(new ApiResponse<>("Debtors retrieved successfully", debtors, true, HttpStatus.OK.value()));
    }
    @GetMapping("/deposits")
    @Operation(summary = "Get all deposits", description = "Get list of all deposits in the system")
    public ResponseEntity<ApiResponse<List<DepositResponse>>> getAllDeposits() {
        List<DepositResponse> deposits = depositService.getAllDeposits();
        return ResponseEntity.ok(new ApiResponse<>("Deposits retrieved successfully", deposits, true, HttpStatus.OK.value()));
    }
    @GetMapping("/loans")
    @Operation(summary = "Get all loans", description = "Get list of all loans in the system")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getAllLoans() {
        List<LoanResponse> loans = loanService.getAllLoans();
        return ResponseEntity.ok(new ApiResponse<>("Loans retrieved successfully", loans, true, HttpStatus.OK.value()));
    }
    @GetMapping("/payments")
    @Operation(summary = "Get all payments", description = "Get list of all payments in the system")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(new ApiResponse<>("Payments retrieved successfully", payments, true, HttpStatus.OK.value()));
    }
    @PutMapping("/loan/{loanId}/status")
    @Operation(summary = "Update loan status", description = "Update the status of a loan")
    public ResponseEntity<ApiResponse<LoanResponse>> updateLoanStatus(
            @PathVariable Long loanId,
            @RequestParam LoanStatus status) {
        LoanResponse loan = loanService.updateLoanStatus(loanId, status);
        return ResponseEntity.ok(new ApiResponse<>("Loan status updated successfully", loan, true, HttpStatus.OK.value()));
    }
    @PutMapping("/user/{userId}/role")
    @Operation(summary = "Update user role", description = "Change a user's role")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserRole(
            @PathVariable Long userId,
            @RequestParam Role role) {
        UserResponse user = userService.updateUserRole(userId, role);
        return ResponseEntity.ok(new ApiResponse<>("User role updated successfully", user, true, HttpStatus.OK.value()));
    }
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user details", description = "Get details of a specific user")
    public ResponseEntity<ApiResponse<UserResponse>> getUserDetails(@PathVariable Long userId) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(new ApiResponse<>("User retrieved successfully", user, true, HttpStatus.OK.value()));
    }
}
