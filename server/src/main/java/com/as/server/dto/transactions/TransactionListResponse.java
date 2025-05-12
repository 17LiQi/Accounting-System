package com.as.server.dto.transactions;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import javax.annotation.Generated;

/**
 * TransactionListResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-07T20:09:16.831236500+08:00[Asia/Shanghai]")
public class TransactionListResponse   {

  @JsonProperty("transactions")
  @Valid
  private List<TransactionDTO> transactions = new ArrayList<>();

  @JsonProperty("total")
  private Integer total;

  public TransactionListResponse transactions(List<TransactionDTO> transactions) {
    this.transactions = transactions;
    return this;
  }

  public TransactionListResponse addTransactionsItem(TransactionDTO transactionsItem) {
    this.transactions.add(transactionsItem);
    return this;
  }

  /**
   * Get transactions
   * @return transactions
  */
  @NotNull @Valid 
  @Schema(name = "transactions", required = true)
  public List<TransactionDTO> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<TransactionDTO> transactions) {
    this.transactions = transactions;
  }

  public TransactionListResponse total(Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
  */
  @NotNull 
  @Schema(name = "total", required = true)
  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionListResponse transactionListResponse = (TransactionListResponse) o;
    return Objects.equals(this.transactions, transactionListResponse.transactions) &&
        Objects.equals(this.total, transactionListResponse.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactions, total);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionListResponse {\n");
    sb.append("    transactions: ").append(toIndentedString(transactions)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

