package com.backbase.accelerators.fics.model;

import com.backbase.accelerators.fics.serializers.CurrencySerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MortgageAccountItem {

    @JsonProperty("LastPaymentDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yy")
    private LocalDate lastPaymentDate;

    @JsonProperty("InterestRate")
    private BigDecimal interestRate;

    @JsonProperty("PrincipalPaidYearToDate")
    @JsonDeserialize(using = CurrencySerializer.class)
    private BigDecimal principalPaidYearToDate;

    @JsonProperty("InterestPaidYearToDate")
    @JsonDeserialize(using = CurrencySerializer.class)
    private BigDecimal interestPaidYearToDate;

    @JsonProperty("DateOfNote")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yy")
    private LocalDate dateOfNote;

    @JsonProperty("DueDateNextPayment")
    private String dueDateNextPayment;

    @JsonProperty("TotalPaymentDue")
    @JsonDeserialize(using = CurrencySerializer.class)
    private BigDecimal totalPaymentDue;

    @JsonProperty("Principal")
    @JsonDeserialize(using = CurrencySerializer.class)
    private BigDecimal principal;

    @JsonProperty("EscrowBalance")
    @JsonDeserialize(using = CurrencySerializer.class)
    private BigDecimal escrowBalance;

    @JsonProperty("fullAccountNumber")
    private String accountNumber;

    @JsonProperty("bankAlias")
    private String bankAlias;
}
