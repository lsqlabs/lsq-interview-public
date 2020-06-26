package com.lsq.interview.web;

import com.lsq.interview.service.DuplicateInvoicesException;
import com.lsq.interview.service.InvoiceUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UploadController {

  private final InvoiceUploadService invoiceUploadService;

  @Autowired
  public UploadController(InvoiceUploadService invoiceUploadService) {
    this.invoiceUploadService = invoiceUploadService;
  }

  @PostMapping("/upload")
  public void upload(@RequestParam("file") MultipartFile file) throws IOException {
    invoiceUploadService.uploadInvoices(file.getInputStream());
  }

  @ExceptionHandler(DuplicateInvoicesException.class)
  public ResponseEntity<String> handleDuplicate(DuplicateInvoicesException ex) {
    return new ResponseEntity<>(
        "Duplicate invoices found: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
