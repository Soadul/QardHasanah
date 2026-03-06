package com.qard.QardHasanah.service;

import com.qard.QardHasanah.dto.*;
import com.qard.QardHasanah.entity.*;
import com.qard.QardHasanah.repository.LoanRepository;
import com.qard.QardHasanah.repository.PaymentRepository;
import com.qard.QardHasanah.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new loan (by depositor or admin)
     */
    public LoanResponse createLoan(Long depositorId, LoanRequest request) {
        User depositor = userRepository.findById(depositorId)
                .orElseThrow(() -> new IllegalArgumentException("Depositor not found"));

        if (depositor.getRole() != Role.DEPOSITOR && depositor.getRole() != Role.SUPER_ADMIN) {
            throw new IllegalArgumentException("User is not authorized to create loans");
        }

        User debtor = userRepository.findById(request.getDebtorId())
                .orElseThrow(() -> new IllegalArgumentException("Debtor not found"));

        if (debtor.getRole() != Role.DEBTOR) {
            throw new IllegalArgumentException("Target user is not a debtor");
        }

        Loan loan = new Loan();
        loan.setDebtorId(request.getDebtorId());
        loan.setDepositorId(depositorId);
        loan.setTotalAmount(request.getTotalAmount());
        loan.setTotalPaid(BigDecimal.ZERO);
        loan.setDurationMonths(request.getDurationMonths());
        loan.setReason(request.getReason());
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setLoanDate(System.currentTimeMillis());
        loan.setCreatedAt(System.currentTimeMillis());
        loan.setUpdatedAt(System.currentTimeMillis());

        // Calculate monthly EMI if duration is provided
        if (request.getDurationMonths() != null && request.getDurationMonths() > 0) {
            BigDecimal monthlyEmi = request.getTotalAmount()
                    .divide(BigDecimal.valueOf(request.getDurationMonths()), 2, RoundingMode.CEILING);
            loan.setMonthlyEmi(monthlyEmi);
        }

        Loan savedLoan = loanRepository.save(loan);
        return convertToResponse(savedLoan);
    }

    /**
     * Get loans by debtor ID
     */
    public List<LoanResponse> getLoansByDebtorId(Long debtorId) {
        return loanRepository.findByDebtorIdOrderByLoanDateDesc(debtorId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get loans by depositor ID
     */
    public List<LoanResponse> getLoansByDepositorId(Long depositorId) {
        return loanRepository.findByDepositorIdOrderByLoanDateDesc(depositorId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get debtor dashboard
     */
    public DebtorDashboardResponse getDebtorDashboard(Long debtorId) {
        User debtor = userRepository.findById(debtorId)
                .orElseThrow(() -> new IllegalArgumentException("Debtor not found"));

        BigDecimal totalDebt = loanRepository.getTotalDebtByDebtorId(debtorId);
        BigDecimal totalPaid = loanRepository.getTotalPaidByDebtorId(debtorId);
        BigDecimal remainingBalance = totalDebt.subtract(totalPaid);

        List<Loan> loans = loanRepository.findByDebtorIdOrderByLoanDateDesc(debtorId);
        long activeLoansCount = loans.stream()
                .filter(l -> l.getStatus() == LoanStatus.ACTIVE)
                .count();

        // Calculate next EMI (sum of all active loan EMIs)
        BigDecimal nextEmiAmount = loans.stream()
                .filter(l -> l.getStatus() == LoanStatus.ACTIVE)
                .map(l -> l.getMonthlyEmi() != null ? l.getMonthlyEmi() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<LoanResponse> loanResponses = loans.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        List<PaymentResponse> recentPayments = paymentRepository.findAllPaymentsByDebtorId(debtorId)
                .stream()
                .limit(10)
                .map(this::convertPaymentToResponse)
                .collect(Collectors.toList());

        DebtorDashboardResponse dashboard = new DebtorDashboardResponse();
        dashboard.setUser(convertUserToResponse(debtor));
        dashboard.setTotalDebt(totalDebt);
        dashboard.setTotalPaid(totalPaid);
        dashboard.setRemainingBalance(remainingBalance);
        dashboard.setNextEmiAmount(nextEmiAmount);
        dashboard.setActiveLoans(activeLoansCount);
        dashboard.setLoans(loanResponses);
        dashboard.setRecentPayments(recentPayments);

        return dashboard;
    }

    /**
     * Get loan by ID
     */
    public LoanResponse getLoanById(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        return convertToResponse(loan);
    }

    /**
     * Get all loans (admin)
     */
    public List<LoanResponse> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update loan status
     */
    public LoanResponse updateLoanStatus(Long loanId, LoanStatus status) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        loan.setStatus(status);
        loan.setUpdatedAt(System.currentTimeMillis());

        Loan updatedLoan = loanRepository.save(loan);
        return convertToResponse(updatedLoan);
    }

    private LoanResponse convertToResponse(Loan loan) {
        User debtor = userRepository.findById(loan.getDebtorId()).orElse(null);
        User depositor = userRepository.findById(loan.getDepositorId()).orElse(null);

        String debtorName = debtor != null ? debtor.getFirstName() + " " + debtor.getLastName() : "Unknown";
        String depositorName = depositor != null ? depositor.getFirstName() + " " + depositor.getLastName() : "Unknown";

        BigDecimal remainingBalance = loan.getTotalAmount().subtract(loan.getTotalPaid());

        return new LoanResponse(
                loan.getId(),
                loan.getDebtorId(),
                debtorName,
                loan.getDepositorId(),
                depositorName,
                loan.getTotalAmount(),
                loan.getTotalPaid(),
                remainingBalance,
                loan.getMonthlyEmi(),
                loan.getDurationMonths(),
                loan.getReason(),
                loan.getStatus(),
                loan.getLoanDate(),
                loan.getCreatedAt()
        );
    }

    private PaymentResponse convertPaymentToResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getLoanId(),
                payment.getAmount(),
                payment.getDescription(),
                payment.getPaymentDate(),
                payment.getCreatedAt()
        );
    }

    private UserResponse convertUserToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getIsActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}

