package com.qard.QardHasanah.service;

import com.qard.QardHasanah.dto.PaymentRequest;
import com.qard.QardHasanah.dto.PaymentResponse;
import com.qard.QardHasanah.entity.Loan;
import com.qard.QardHasanah.entity.LoanStatus;
import com.qard.QardHasanah.entity.Payment;
import com.qard.QardHasanah.repository.LoanRepository;
import com.qard.QardHasanah.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private LoanRepository loanRepository;

    /**
     * Make a payment towards a loan
     */
    public PaymentResponse makePayment(Long debtorId, PaymentRequest request) {
        Loan loan = loanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        // Verify the debtor owns this loan
        if (!loan.getDebtorId().equals(debtorId)) {
            throw new IllegalArgumentException("You are not authorized to make payments on this loan");
        }

        if (loan.getStatus() == LoanStatus.COMPLETED) {
            throw new IllegalArgumentException("This loan is already fully paid");
        }

        // Create payment
        Payment payment = new Payment();
        payment.setLoanId(request.getLoanId());
        payment.setAmount(request.getAmount());
        payment.setDescription(request.getDescription());
        payment.setPaymentDate(System.currentTimeMillis());
        payment.setCreatedAt(System.currentTimeMillis());

        Payment savedPayment = paymentRepository.save(payment);

        // Update loan total paid
        BigDecimal newTotalPaid = loan.getTotalPaid().add(request.getAmount());
        loan.setTotalPaid(newTotalPaid);
        loan.setUpdatedAt(System.currentTimeMillis());

        // Check if loan is fully paid
        if (newTotalPaid.compareTo(loan.getTotalAmount()) >= 0) {
            loan.setStatus(LoanStatus.COMPLETED);
        }

        loanRepository.save(loan);

        return convertToResponse(savedPayment);
    }

    /**
     * Get payments by loan ID
     */
    public List<PaymentResponse> getPaymentsByLoanId(Long loanId) {
        return paymentRepository.findByLoanIdOrderByPaymentDateDesc(loanId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all payments for a debtor
     */
    public List<PaymentResponse> getPaymentsByDebtorId(Long debtorId) {
        return paymentRepository.findAllPaymentsByDebtorId(debtorId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all payments (admin)
     */
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private PaymentResponse convertToResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getLoanId(),
                payment.getAmount(),
                payment.getDescription(),
                payment.getPaymentDate(),
                payment.getCreatedAt()
        );
    }
}

