package com.lsq.interview.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"invoiceId", "supplierId", "refFundingId"}))
public class Invoice {
  @Id private String invoiceId;

  @Column(nullable = false)
  private String supplierId;

  @Column
  public String refFundingId;

  @Column(nullable = false)
  public LocalDate invoiceDate;

  @Column(nullable = false)
  public BigDecimal invoiceAmount;

  @Column(nullable = false)
  public int terms;

  @Column public LocalDate paymentDate;

  @Column public BigDecimal paymentAmount;

  @Column public InvoiceStatus invoiceStatus;

  public InvoiceStatus getInvoiceStatus() {
    return invoiceStatus;
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

  public String getRefFundingId() {
    return refFundingId;
  }
}
