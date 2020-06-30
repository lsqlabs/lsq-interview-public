package com.lsq.interview.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"invoiceId", "supplierId"}))
public class Invoice {
  @Id private String invoiceId;

  @Column(nullable = false)
  private String supplierId;

  @Column(nullable = false)
  private LocalDate invoiceDate;

  @Column(nullable = false)
  private BigDecimal invoiceAmount;

  @Column(nullable = false)
  private int terms;

  @Column private LocalDate paymentDate;

  @Column private BigDecimal paymentAmount;

  @Enumerated(EnumType.STRING)
  @Column
  private FundingStatus fundingStatus;

  @Column private BigDecimal fundedAmount;

  @Column private BigDecimal feeAmount;

  public enum FundingStatus {
    APPROVED,
    REJECTED;
  }

  public String getInvoiceId() {
    return invoiceId;
  }

  public String getSupplierId() {
    return supplierId;
  }

  public LocalDate getInvoiceDate() {
    return invoiceDate;
  }

  public BigDecimal getInvoiceAmount() {
    return invoiceAmount;
  }

  public int getTerms() {
    return terms;
  }

  public LocalDate getPaymentDate() {
    return paymentDate;
  }

  public BigDecimal getPaymentAmount() {
    return paymentAmount;
  }

  public FundingStatus getFundingStatus() {
    return fundingStatus;
  }

  public void setFundingStatus(FundingStatus fundingStatus) {
    this.fundingStatus = fundingStatus;
  }

  public BigDecimal getFundedAmount() {
    return fundedAmount;
  }

  public void setFundedAmount(BigDecimal fundedAmount) {
    this.fundedAmount = fundedAmount;
  }

  public BigDecimal getFeeAmount() {
    return feeAmount;
  }

  public void setFeeAmount(BigDecimal revenueAmount) {
    this.feeAmount = revenueAmount;
  }
}
