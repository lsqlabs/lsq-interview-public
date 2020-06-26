package com.lsq.interview.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.lsq.interview.data.InvoiceRepository;
import com.lsq.interview.model.Invoice;
import org.apache.commons.lang3.tuple.Pair;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class InvoiceUploadService {

  private final InvoiceRepository invoiceRepository;

  private static final CsvSchema INVOICE_CSV_SCHEMA =
      CsvSchema.builder()
          .addColumn("Supplier Id")
          .addColumn("Invoice Id")
          .addColumn("Invoice Date")
          .addNumberColumn("Invoice Amount")
          .addNumberColumn("Terms")
          .addColumn("Payment Date")
          .addNumberColumn("Payment Amount")
          .build()
          .withHeader();

  @Autowired
  public InvoiceUploadService(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  /**
   * Saves invoices from a CSV file to the database.
   *
   * @param csvStream input stream over a CSV file
   * @throws IOException if an IO error occurred
   * @throws DuplicateInvoicesException if the CSV file contains duplicate invoices
   */
  public void uploadInvoices(InputStream csvStream) throws IOException {
    var csvMapper = new CsvMapper();
    csvMapper.findAndRegisterModules();

    var invoiceIterator =
        csvMapper.readerFor(InvoiceCsv.class).with(INVOICE_CSV_SCHEMA).readValues(csvStream);

    var mapper = new ModelMapper();
    mapper
        .getConfiguration()
        .setFieldAccessLevel(AccessLevel.PRIVATE)
        .setFieldMatchingEnabled(true);

    var invoices = new ArrayList<Invoice>();
    invoiceIterator.forEachRemaining(i -> invoices.add(mapper.map(i, Invoice.class)));

    detectDuplicates(invoices);

    invoiceRepository.saveAll(invoices);
  }

  private static void detectDuplicates(List<Invoice> invoices) {
    var idsSet = new HashSet<Pair<String, String>>();
    var duplicates = new HashSet<Invoice>();

    invoices.forEach(
        invoice -> {
          var ids = Pair.of(invoice.getSupplierId(), invoice.getInvoiceId());

          if (idsSet.contains(ids)) {
            duplicates.add(invoice);
          }

          idsSet.add(ids);
        });

    if (!duplicates.isEmpty()) {
      throw new DuplicateInvoicesException(duplicates);
    }
  }

  @SuppressWarnings("unused")
  private static class InvoiceCsv {
    @JsonProperty("Supplier Id")
    public String supplierId;

    @JsonProperty("Invoice Id")
    public String invoiceId;

    @JsonProperty("Invoice Date")
    public LocalDate invoiceDate;

    @JsonProperty("Invoice Amount")
    public BigDecimal invoiceAmount;

    @JsonProperty("Terms")
    public int terms;

    @JsonProperty("Payment Date")
    public LocalDate paymentDate;

    @JsonProperty("Payment Amount")
    public BigDecimal paymentAmount;
  }
}
