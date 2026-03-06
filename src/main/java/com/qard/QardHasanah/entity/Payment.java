package com.qard.QardHasanah.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Loan ID is required")
    @Column(name = "loan_id", nullable = false)
    private Long loanId;
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    @Column(length = 500)
    private String description;
    @Column(name = "payment_date", nullable = false)
    private Long paymentDate;
    @Column(name = "created_at")
    private Long createdAt = System.currentTimeMillis();
}
