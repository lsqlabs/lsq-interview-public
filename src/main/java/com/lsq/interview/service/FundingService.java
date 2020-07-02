package com.lsq.interview.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsq.interview.data.FundingRepository;
import com.lsq.interview.data.InvoiceRepository;
import com.lsq.interview.model.Funding;
import com.lsq.interview.model.Invoice;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.lsq.interview.model.InvoiceStatus.APPROVED;
import static com.lsq.interview.model.InvoiceStatus.REJECTED;

@Service
public class FundingService {
    private final FundingRepository fundingRepository;
    private final InvoiceRepository invoiceRepository;
    private final static BigDecimal FEE_RATE = new BigDecimal(0.02);

    public FundingService(FundingRepository fundingRepository, InvoiceRepository invoiceRepository) {
        this.fundingRepository = fundingRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public RequestFundingResponse requestFunding(List<String> invoiceKeys) {
        List<Invoice> invoices = invoiceKeys.stream().map(this::fetchInvoice).collect(Collectors.toList());
        RequestFundingResponse requestFundingResponse = new RequestFundingResponse();
        Funding funding = new Funding();

        funding.fundingId = UUID.randomUUID().toString();
        funding.supplierId = invoices.get(0).getSupplierId();

        List<Invoice> processedInvoices =
                invoices.stream().map(this::determineInvoiceEligibilities).collect(Collectors.toList());
        BigDecimal approvedAmount = processedInvoices.stream().map(invoice -> {
            BigDecimal approvedAmountForInvoice = BigDecimal.ZERO;
            if (APPROVED.equals(invoice.invoiceStatus)) {
                invoice.refFundingId = funding.fundingId;
                approvedAmountForInvoice = invoice.getInvoiceAmount();
            }
            return approvedAmountForInvoice;
        }).reduce(BigDecimal::add).get();

        BigDecimal fee = approvedAmount.multiply(FEE_RATE);

        funding.feeAmount = fee;
        funding.fundedAmount = approvedAmount.subtract(fee);
        funding.dateFunded = LocalDate.now();

        requestFundingResponse.funding = funding;
        requestFundingResponse.invoices = invoices;

        invoiceRepository.saveAll(processedInvoices);
        fundingRepository.save(funding);

        return requestFundingResponse;
    }

    public Invoice determineInvoiceEligibilities(Invoice invoice) {
        LocalDate now = LocalDate.now();
        BigDecimal invoiceAmount = invoice.getInvoiceAmount();
        if (invoice.getPaymentDate() == null && now.isBefore(invoice.getInvoiceDate().plusDays(invoice.terms).minusDays(15))
                && !invoiceAmount.add(invoiceAmount).equals(BigDecimal.ZERO)) {
            invoice.invoiceStatus = APPROVED;
        } else {
            invoice.invoiceStatus = REJECTED;
        }
        return invoice;
    }

    public Invoice fetchInvoice(String invoiceKey) {
        return invoiceRepository.findById(invoiceKey).get();
    }

    public class RequestFundingResponse {
        @JsonProperty
        Funding funding;

        @JsonProperty
        List<Invoice> invoices;
    }
}

