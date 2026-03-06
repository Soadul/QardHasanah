package com.qard.QardHasanah.repository;

import com.qard.QardHasanah.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByLoanIdOrderByPaymentDateDesc(Long loanId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.loanId = :loanId")
    BigDecimal getTotalPaymentsByLoanId(@Param("loanId") Long loanId);

    @Query("SELECT p FROM Payment p WHERE p.loanId IN (SELECT l.id FROM Loan l WHERE l.debtorId = :debtorId) ORDER BY p.paymentDate DESC")
    List<Payment> findAllPaymentsByDebtorId(@Param("debtorId") Long debtorId);
}

