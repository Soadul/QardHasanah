package com.qard.QardHasanah.service;

import com.qard.QardHasanah.dto.DepositRequest;
import com.qard.QardHasanah.dto.DepositResponse;
import com.qard.QardHasanah.dto.DepositorDashboardResponse;
import com.qard.QardHasanah.dto.LoanResponse;
import com.qard.QardHasanah.dto.UserResponse;
import com.qard.QardHasanah.entity.Deposit;
import com.qard.QardHasanah.entity.Loan;
import com.qard.QardHasanah.entity.Role;
import com.qard.QardHasanah.entity.User;
import com.qard.QardHasanah.repository.DepositRepository;
import com.qard.QardHasanah.repository.LoanRepository;
import com.qard.QardHasanah.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new deposit
     */
    public DepositResponse createDeposit(Long depositorId, DepositRequest request) {
        User depositor = userRepository.findById(depositorId)
                .orElseThrow(() -> new IllegalArgumentException("Depositor not found"));

        if (depositor.getRole() != Role.DEPOSITOR && depositor.getRole() != Role.SUPER_ADMIN) {
            throw new IllegalArgumentException("User is not authorized to make deposits");
        }

        Deposit deposit = new Deposit();
        deposit.setDepositorId(depositorId);
        deposit.setAmount(request.getAmount());
        deposit.setDescription(request.getDescription());
        deposit.setDepositDate(System.currentTimeMillis());
        deposit.setCreatedAt(System.currentTimeMillis());
        deposit.setUpdatedAt(System.currentTimeMillis());

        Deposit savedDeposit = depositRepository.save(deposit);
        return convertToResponse(savedDeposit);
    }

    /**
     * Get all deposits for a depositor
     */
    public List<DepositResponse> getDepositsByDepositorId(Long depositorId) {
        return depositRepository.findByDepositorIdOrderByDepositDateDesc(depositorId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get depositor dashboard
     */
    public DepositorDashboardResponse getDepositorDashboard(Long depositorId) {
        User depositor = userRepository.findById(depositorId)
                .orElseThrow(() -> new IllegalArgumentException("Depositor not found"));

        BigDecimal totalDeposited = depositRepository.getTotalDepositsByDepositorId(depositorId);
        BigDecimal totalLent = loanRepository.getTotalLentByDepositorId(depositorId);
        BigDecimal availableBalance = totalDeposited.subtract(totalLent);

        List<Loan> loans = loanRepository.findByDepositorIdOrderByLoanDateDesc(depositorId);
        long activeBorrowers = loans.stream()
                .map(Loan::getDebtorId)
                .distinct()
                .count();

        List<DepositResponse> recentDeposits = depositRepository.findByDepositorIdOrderByDepositDateDesc(depositorId)
                .stream()
                .limit(10)
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        List<LoanResponse> activeLoans = loans.stream()
                .map(this::convertLoanToResponse)
                .collect(Collectors.toList());

        DepositorDashboardResponse dashboard = new DepositorDashboardResponse();
        dashboard.setUser(convertUserToResponse(depositor));
        dashboard.setTotalDeposited(totalDeposited);
        dashboard.setTotalLent(totalLent);
        dashboard.setAvailableBalance(availableBalance);
        dashboard.setActiveBorrowers(activeBorrowers);
        dashboard.setRecentDeposits(recentDeposits);
        dashboard.setActiveLoans(activeLoans);

        return dashboard;
    }

    /**
     * Get all deposits (admin)
     */
    public List<DepositResponse> getAllDeposits() {
        return depositRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private DepositResponse convertToResponse(Deposit deposit) {
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

