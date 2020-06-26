package com.lsq.interview.service;

import com.lsq.interview.model.Invoice;

import java.util.Collection;
import java.util.stream.Collectors;

public class DuplicateInvoicesException extends RuntimeException {
  public DuplicateInvoicesException(Collection<Invoice> duplicates) {
    super(getMessage(duplicates));
  }

  private static String getMessage(Collection<Invoice> duplicates) {
    return duplicates.stream()
        .map(
            invoice ->
                String.format(
                    "supplierId=%s, invoiceId=%s", invoice.getSupplierId(), invoice.getInvoiceId()))
        .collect(Collectors.joining(";"));
  }
}
