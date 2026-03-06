package com.qard.QardHasanah.controller;
import com.qard.QardHasanah.dto.*;
import com.qard.QardHasanah.security.JwtUtil;
import com.qard.QardHasanah.service.LoanService;
import com.qard.QardHasanah.service.PaymentService;
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
@RequestMapping("/api/debtor")
@CrossOrigin(origins = "*")
@Tag(name = "Debtor", description = "APIs for debtors to view loans and make payments")
@SecurityRequirement(name = "bearerAuth")
public class DebtorController {
    @Autowired
    private LoanService loanService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private JwtUtil jwtUtil;
    @GetMapping("/dashboard")
    @Operation(summary = "Get debtor dashboard", description = "Get dashboard with loan summary, payments, and depositor info")
    @PreAuthorize("hasRole('DEBTOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<DebtorDashboardResponse>> getDashboard(
            @RequestHeader("Authorization") String authHeader) {
        Long debtorId = extractUserIdFromToken(authHeader);
        DebtorDashboardResponse dashboard = loanService.getDebtorDashboard(debtorId);
        return ResponseEntity.ok(new ApiResponse<>("Dashboard retrieved successfully", dashboard, true, HttpStatus.OK.value()));
    }
    @GetMapping("/my-loans")
    @Operation(summary = "Get my loans", description = "Get all loans for the logged-in debtor")
    @PreAuthorize("hasRole('DEBTOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getMyLoans(
            @RequestHeader("Authorization") String authHeader) {
        Long debtorId = extractUserIdFromToken(authHeader);
        List<LoanResponse> loans = loanService.getLoansByDebtorId(debtorId);
        return ResponseEntity.ok(new ApiResponse<>("Loans retrieved successfully", loans, true, HttpStatus.OK.value()));
    }
    @GetMapping("/loan/{loanId}")
    @Operation(summary = "Get loan details", description = "Get details of a specific loan")
    @PreAuthorize("hasRole('DEBTOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<LoanResponse>> getLoanDetails(@PathVariable Long loanId) {
        LoanResponse loan = loanService.getLoanById(loanId);
        return ResponseEntity.ok(new ApiResponse<>("Loan retrieved successfully", loan, true, HttpStatus.OK.value()));
    }
    @PostMapping("/payment")
    @Operation(summary = "Make a payment", description = "Debtor makes a payment towards a loan")
    @PreAuthorize("hasRole('DEBTOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<PaymentResponse>> makePayment(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody PaymentRequest request) {
        Long debtorId = extractUserIdFromToken(authHeader);
        PaymentResponse payment = paymentService.makePayment(debtorId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Payment made successfully", payment, true, HttpStatus.CREATED.value()));
    }
    @GetMapping("/my-payments")
    @Operation(summary = "Get my payments", description = "Get all payments made by the logged-in debtor")
    @PreAuthorize("hasRole('DEBTOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getMyPayments(
            @RequestHeader("Authorization") String authHeader) {
        Long debtorId = extractUserIdFromToken(authHeader);
        List<PaymentResponse> payments = paymentService.getPaymentsByDebtorId(debtorId);
        return ResponseEntity.ok(new ApiResponse<>("Payments retrieved successfully", payments, true, HttpStatus.OK.value()));
    }
    @GetMapping("/loan/{loanId}/payments")
    @Operation(summary = "Get loan payments", description = "Get all payments for a specific loan")
    @PreAuthorize("hasRole('DEBTOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getLoanPayments(@PathVariable Long loanId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByLoanId(loanId);
        return ResponseEntity.ok(new ApiResponse<>("Payments retrieved successfully", payments, true, HttpStatus.OK.value()));
    }
    private Long extractUserIdFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.extractUserId(token);
    }
}
