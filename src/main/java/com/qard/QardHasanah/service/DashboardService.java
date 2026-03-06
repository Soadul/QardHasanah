package com.qard.QardHasanah.service;

import com.qard.QardHasanah.dto.*;
import com.qard.QardHasanah.entity.*;
import com.qard.QardHasanah.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Get admin dashboard with all statistics
     */
    public AdminDashboardResponse getAdminDashboard() {
        BigDecimal totalDeposits = depositRepository.getTotalDeposits();
        BigDecimal totalLoans = loanRepository.getTotalLoans();
        BigDecimal totalRecovered = loanRepository.getTotalRecovered();
        BigDecimal totalOutstandingDebt = loanRepository.getTotalOutstandingDebt();
        BigDecimal availableFunds = totalDeposits.subtract(totalLoans).add(totalRecovered);

        Long totalDepositors = userRepository.countByRole(Role.DEPOSITOR);
        Long totalDebtors = userRepository.countByRole(Role.DEBTOR);
        Long activeLoansCount = loanRepository.countByStatus(LoanStatus.ACTIVE);
        Long completedLoansCount = loanRepository.countByStatus(LoanStatus.COMPLETED);

        List<UserResponse> depositors = userRepository.findByRole(Role.DEPOSITOR)
                .stream()
                .map(this::convertUserToResponse)
                .collect(Collectors.toList());

        List<UserResponse> debtors = userRepository.findByRole(Role.DEBTOR)
                .stream()
                .map(this::convertUserToResponse)
                .collect(Collectors.toList());

        List<LoanResponse> recentLoans = loanRepository.findAll()
                .stream()
                .sorted((a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()))
                .limit(10)
                .map(this::convertLoanToResponse)
                .collect(Collectors.toList());

        List<DepositResponse> recentDeposits = depositRepository.findAll()
                .stream()
                .sorted((a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()))
                .limit(10)
                .map(this::convertDepositToResponse)
                .collect(Collectors.toList());

        AdminDashboardResponse dashboard = new AdminDashboardResponse();
        dashboard.setTotalDeposits(totalDeposits);
        dashboard.setTotalLoans(totalLoans);
        dashboard.setTotalRecovered(totalRecovered);
        dashboard.setTotalOutstandingDebt(totalOutstandingDebt);
        dashboard.setAvailableFunds(availableFunds);
        dashboard.setTotalDepositors(totalDepositors);
        dashboard.setTotalDebtors(totalDebtors);
        dashboard.setActiveLoansCount(activeLoansCount);
        dashboard.setCompletedLoansCount(completedLoansCount);
        dashboard.setDepositors(depositors);
        dashboard.setDebtors(debtors);
        dashboard.setRecentLoans(recentLoans);
        dashboard.setRecentDeposits(recentDeposits);

        return dashboard;
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

    private LoanResponse convertLoanToResponse(Loan loan) {
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

    private DepositResponse convertDepositToResponse(Deposit deposit) {
        User depositor = userRepository.findById(deposit.getDepositorId()).orElse(null);
        String depositorName = depositor != null ? depositor.getFirstName() + " " + depositor.getLastName() : "Unknown";

        return new DepositResponse(
                deposit.getId(),
                deposit.getDepositorId(),
                depositorName,
                deposit.getAmount(),
                deposit.getDescription(),
                deposit.getDepositDate(),
                deposit.getCreatedAt()
        );
    }
}

