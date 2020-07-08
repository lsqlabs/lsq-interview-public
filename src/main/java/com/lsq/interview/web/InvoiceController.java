package com.lsq.interview.web;

import com.lsq.interview.data.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@RestController
public class InvoiceController {
  private final InvoiceRepository invoiceRepository;

  @Autowired
  public InvoiceController(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  @GetMapping("/ListSupplierSummary")
  public SupplierInvoiceSummary listSupplierSummary(@RequestParam("supplierId") String supplierId) {
    var summary = new SupplierInvoiceSummary();

    summary.totalInvoices = invoiceRepository.countForSupplier(supplierId);
    summary.openInvoices = invoiceRepository.countOpenInvoicesForSupplier(supplierId);
    summary.lateInvoices = invoiceRepository.countLateInvoicesForSupplier(supplierId);

    summary.totalOpenInvoiceAmount =
        defaultIfNull(
            invoiceRepository.totalOpenInvoiceAmountForSupplier(supplierId), BigDecimal.ZERO);

    summary.totalLateInvoiceAmount =
        defaultIfNull(
            invoiceRepository.totalLateInvoiceAmountForSupplier(supplierId), BigDecimal.ZERO);

    return summary;
  }

  private static class SupplierInvoiceSummary {
    public long totalInvoices;
    public long openInvoices;
    public long lateInvoices;
    public BigDecimal totalOpenInvoiceAmount;
    public BigDecimal totalLateInvoiceAmount;
  }
}
