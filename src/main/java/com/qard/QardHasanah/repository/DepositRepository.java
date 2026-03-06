package com.qard.QardHasanah.repository;

import com.qard.QardHasanah.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {

    List<Deposit> findByDepositorIdOrderByDepositDateDesc(Long depositorId);

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Deposit d WHERE d.depositorId = :depositorId")
    BigDecimal getTotalDepositsByDepositorId(@Param("depositorId") Long depositorId);

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Deposit d")
    BigDecimal getTotalDeposits();

    @Query("SELECT COUNT(DISTINCT d.depositorId) FROM Deposit d")
    Long countActiveDepositors();
}

