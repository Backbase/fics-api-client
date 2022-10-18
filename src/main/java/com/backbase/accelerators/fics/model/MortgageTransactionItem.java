package com.backbase.accelerators.fics.model;

import com.backbase.accelerators.fics.serializers.CurrencySerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class MortgageTransactionItem {

    @JsonProperty("_ID")
    private String id;

    @JsonProperty("PaidDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yy")
    private LocalDate paidDate;

    @JsonProperty("PaymentAmount")
    @JsonDeserialize(using = CurrencySerializer.class)
    private BigDecimal paymentAmount;

    @JsonProperty("TransactionDescription")
    private String transactionDescription;

    @JsonProperty("PrincipalBalance")
    @JsonDeserialize(using = CurrencySerializer.class)
    private BigDecimal principalBalance;

    @JsonProperty("TaxAndInsuranceBalance")
    @JsonDeserialize(using = CurrencySerializer.class)
    private BigDecimal taxAndInsuranceBalance;

    @JsonProperty("Interest")
    @JsonDeserialize(using = CurrencySerializer.class)
    private BigDecimal interest;

    @JsonProperty("InvestorLoanNumber")
    private String loanNumber;

    @JsonProperty("TransactionType")
    private String transactionType;

    @JsonProperty("Principal")
    @JsonDeserialize(using = CurrencySerializer.class)
    private BigDecimal principal;

    @JsonProperty("ReversedFlag")
    private String reversedFlag;

    private String curtailment;
}
