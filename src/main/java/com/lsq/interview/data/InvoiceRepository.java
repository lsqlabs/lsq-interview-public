package com.lsq.interview.data;

import com.lsq.interview.model.Invoice;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<Invoice, String> {}
