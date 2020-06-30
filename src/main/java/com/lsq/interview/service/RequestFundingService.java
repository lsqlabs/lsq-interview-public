package com.lsq.interview.service;

import com.lsq.interview.model.Invoice;
import com.lsq.interview.model.Invoice.FundingStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RequestFundingService {
  private static final BigDecimal feeRate = new BigDecimal("0.02");

  public void request(Iterable<Invoice> invoices) {
    for (var invoice : invoices) {
      var eligible = EligibilityRule.checkAll(invoice);

      if (eligible) {
        invoice.setFundingStatus(FundingStatus.APPROVED);

        var feeAmount = invoice.getInvoiceAmount().multiply(feeRate);

        invoice.setFeeAmount(feeAmount);
        invoice.setFundedAmount(invoice.getInvoiceAmount());
      } else {
        invoice.setFundingStatus(FundingStatus.REJECTED);
      }
    }
  }
}
