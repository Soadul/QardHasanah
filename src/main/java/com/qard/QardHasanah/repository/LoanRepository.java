package com.qard.QardHasanah.repository;

import com.qard.QardHasanah.entity.Loan;
import com.qard.QardHasanah.entity.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByDebtorIdOrderByLoanDateDesc(Long debtorId);

    List<Loan> findByDepositorIdOrderByLoanDateDesc(Long depositorId);

    List<Loan> findByStatus(LoanStatus status);

    @Query("SELECT COALESCE(SUM(l.totalAmount), 0) FROM Loan l WHERE l.debtorId = :debtorId")
    BigDecimal getTotalDebtByDebtorId(@Param("debtorId") Long debtorId);

    @Query("SELECT COALESCE(SUM(l.totalPaid), 0) FROM Loan l WHERE l.debtorId = :debtorId")
    BigDecimal getTotalPaidByDebtorId(@Param("debtorId") Long debtorId);

    @Query("SELECT COALESCE(SUM(l.totalAmount), 0) FROM Loan l WHERE l.depositorId = :depositorId")
    BigDecimal getTotalLentByDepositorId(@Param("depositorId") Long depositorId);

    @Query("SELECT COALESCE(SUM(l.totalAmount), 0) FROM Loan l")
    BigDecimal getTotalLoans();

    @Query("SELECT COALESCE(SUM(l.totalPaid), 0) FROM Loan l")
    BigDecimal getTotalRecovered();

    @Query("SELECT COALESCE(SUM(l.totalAmount - l.totalPaid), 0) FROM Loan l WHERE l.status = 'ACTIVE'")
    BigDecimal getTotalOutstandingDebt();

    @Query("SELECT COUNT(DISTINCT l.debtorId) FROM Loan l")
    Long countActiveDebtors();

    Long countByStatus(LoanStatus status);
}

