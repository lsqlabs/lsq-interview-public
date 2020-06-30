package com.lsq.interview.web;

import com.lsq.interview.data.InvoiceRepository;
import com.lsq.interview.model.Invoice;
import com.lsq.interview.model.Invoice.FundingStatus;
import com.lsq.interview.service.RequestFundingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@RestController
public class InvoiceController {
  private final InvoiceRepository invoiceRepository;
  private final RequestFundingService requestFundingService;

  @Autowired
  public InvoiceController(
      InvoiceRepository invoiceRepository, RequestFundingService requestFundingService) {
    this.invoiceRepository = invoiceRepository;
    this.requestFundingService = requestFundingService;
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

  @PostMapping("/RequestFunding")
  public FundingResponse requestFunding(@RequestBody FundingRequest invoiceIds) {
    var invoices = invoiceRepository.findAllById(invoiceIds.invoiceIds);
    requestFundingService.request(invoices);

    FundingResponse response = new FundingResponse();

    response.totalFundedAmount =
        toStream(invoices)
            .map(i -> Optional.ofNullable(i.getFundedAmount()).orElse(BigDecimal.ZERO))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    response.totalFeeAmount =
        toStream(invoices)
            .map(i -> Optional.ofNullable(i.getFeeAmount()).orElse(BigDecimal.ZERO))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    response.approvedInvoiceIds =
        toStream(invoices)
            .filter(i -> i.getFundingStatus() == FundingStatus.APPROVED)
            .map(Invoice::getInvoiceId)
            .collect(Collectors.toSet());

    response.rejectedInvoiceIds =
        toStream(invoices)
            .filter(i -> i.getFundingStatus() == FundingStatus.REJECTED)
            .map(Invoice::getInvoiceId)
            .collect(Collectors.toSet());

    return response;
  }

  private static <T> Stream<T> toStream(Iterable<T> invoices) {
    return StreamSupport.stream(invoices.spliterator(), false);
  }

  private static class FundingRequest {
    public Set<String> invoiceIds;
  }

  private static class FundingResponse {
    public BigDecimal totalFundedAmount;
    public BigDecimal totalFeeAmount;
    public Set<String> approvedInvoiceIds;
    public Set<String> rejectedInvoiceIds;
  }

  private static class SupplierInvoiceSummary {
    public long totalInvoices;
    public long openInvoices;
    public long lateInvoices;
    public BigDecimal totalOpenInvoiceAmount;
    public BigDecimal totalLateInvoiceAmount;
  }
}
