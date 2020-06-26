package com.lsq.interview.data;

import com.lsq.interview.model.Invoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface InvoiceRepository extends CrudRepository<Invoice, String> {
  @Query("SELECT COUNT(id) FROM Invoice WHERE supplierId=:supplierId")
  long countForSupplier(@Param("supplierId") String supplierId);

  @Query(
      "SELECT COUNT(id) FROM Invoice WHERE supplierId=:supplierId AND COALESCE(paymentAmount, 0) < invoiceAmount")
  long countOpenInvoicesForSupplier(@Param("supplierId") String supplierId);

  @Query(
      "SELECT COUNT(id) FROM Invoice WHERE supplierId=:supplierId AND COALESCE(paymentAmount, 0) < invoiceAmount AND invoiceDate + terms >= now()")
  long countLateInvoicesForSupplier(@Param("supplierId") String supplierId);

  @Query(
      "SELECT SUM(invoiceAmount - COALESCE(paymentAmount, 0)) FROM Invoice WHERE supplierId=:supplierId AND COALESCE(paymentAmount, 0) < invoiceAmount")
  BigDecimal totalOpenInvoiceAmountForSupplier(@Param("supplierId") String supplierId);

  @Query(
      "SELECT SUM(invoiceAmount) FROM Invoice WHERE supplierId=:supplierId AND COALESCE(paymentAmount, 0) < invoiceAmount AND invoiceDate + terms >= now()")
  BigDecimal totalLateInvoiceAmountForSupplier(@Param("supplierId") String supplierId);
}
