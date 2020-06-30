package com.lsq.interview.service;

import com.lsq.interview.model.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;

public interface EligibilityRule {
  Set<EligibilityRule> RULES =
      Set.of(new MustBeUnpaid(), new NotTooOld(), new PositiveInvoiceAmount());

  /**
   * @param invoice the invoice to check this rule against
   * @return true if the rule passes, false otherwise
   */
  boolean check(Invoice invoice);

  static boolean checkAll(Invoice invoice) {
    return RULES.stream().anyMatch(rule -> rule.check(invoice));
  }

  class MustBeUnpaid implements EligibilityRule {
    @Override
    public boolean check(Invoice invoice) {
      return invoice.getPaymentAmount() == null;
    }
  }

  class NotTooOld implements EligibilityRule {
    private static final int MAX_DAYS_BEFORE_TERMS = 15;

    @Override
    public boolean check(Invoice invoice) {
      return invoiceAge(invoice) < invoice.getTerms() - MAX_DAYS_BEFORE_TERMS;
    }

    private static long invoiceAge(Invoice invoice) {
      return DAYS.between(invoice.getInvoiceDate(), LocalDate.now());
    }
  }

  class PositiveInvoiceAmount implements EligibilityRule {
    @Override
    public boolean check(Invoice invoice) {
      return invoice.getInvoiceAmount() != null
          && invoice.getInvoiceAmount().compareTo(BigDecimal.ZERO) > 0;
    }
  }
}
