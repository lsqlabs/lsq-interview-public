package com.lsq.interview.web;

import com.lsq.interview.service.FundingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FundingController {
    private final FundingService fundingService;

    @Autowired
    public FundingController(FundingService fundingService) {
        this.fundingService = fundingService;
    }

    @RequestMapping(value = "/RequestFunding", method = RequestMethod.POST)
    public FundingService.RequestFundingResponse listSupplierSummary(@RequestBody List<String> invoiceKeys) {
        return fundingService.requestFunding(invoiceKeys);
    }
}

