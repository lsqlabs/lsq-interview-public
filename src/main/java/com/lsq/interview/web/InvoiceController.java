package com.lsq.interview.web;

import com.lsq.interview.data.InvoiceRepository;
import com.lsq.interview.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoiceController {
  private final InvoiceRepository invoiceRepository;

  @Autowired
  public InvoiceController(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  @GetMapping("/ListInvoiceSummary")
  public Iterable<Invoice> listInvoiceSummary() {
    return invoiceRepository.findAll();
  }
}
