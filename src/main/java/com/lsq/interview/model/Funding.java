package com.lsq.interview.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"fundingId"}))
public class Funding {
    @Id
    @JsonProperty
    public String fundingId;

    @Column(nullable = false)
    @JsonProperty
    public String supplierId;

    @Column
    @JsonProperty
    public BigDecimal fundedAmount;

    @Column
    @JsonProperty
    public BigDecimal feeAmount;

    @Column
    @JsonProperty
    public LocalDate dateFunded;

    public String getFundingId() {
        return fundingId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public BigDecimal getFundedAmount() {
        return fundedAmount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public LocalDate getDateFunded() {
        return dateFunded;
    }
}
