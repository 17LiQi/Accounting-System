package com.as.server.dto.transactions;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import javax.annotation.Generated;

/**
 * TransactionRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-07T20:09:16.831236500+08:00[Asia/Shanghai]")
public class TransactionRequest   {

  @JsonProperty("time")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime time;

  @JsonProperty("typeId")
  private Integer typeId;

  @JsonProperty("subAccountId")
  private Integer subAccountId;

  @JsonProperty("amount")
  private String amount;

  @JsonProperty("remarks")
  private String remarks;

  public TransactionRequest time(OffsetDateTime time) {
    this.time = time;
    return this;
  }

  /**
   * Get time
   * @return time
  */
  @NotNull @Valid 
  @Schema(name = "time", required = true)
  public OffsetDateTime getTime() {
    return time;
  }

  public void setTime(OffsetDateTime time) {
    this.time = time;
  }

  public TransactionRequest typeId(Integer typeId) {
    this.typeId = typeId;
    return this;
  }

  /**
   * Get typeId
   * @return typeId
  */
  @NotNull 
  @Schema(name = "typeId", required = true)
  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public TransactionRequest subAccountId(Integer subAccountId) {
    this.subAccountId = subAccountId;
    return this;
  }

  /**
   * Get subAccountId
   * @return subAccountId
  */
  @NotNull 
  @Schema(name = "subAccountId", required = true)
  public Integer getSubAccountId() {
    return subAccountId;
  }

  public void setSubAccountId(Integer subAccountId) {
    this.subAccountId = subAccountId;
  }

  public TransactionRequest amount(String amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
  */
  @NotNull @Pattern(regexp = "^\\d+(\\.\\d{2})?$") 
  @Schema(name = "amount", required = true)
  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public TransactionRequest remarks(String remarks) {
    this.remarks = remarks;
    return this;
  }

  /**
   * Get remarks
   * @return remarks
  */
  @Size(max = 255) 
  @Schema(name = "remarks", required = false)
  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionRequest transactionRequest = (TransactionRequest) o;
    return Objects.equals(this.time, transactionRequest.time) &&
        Objects.equals(this.typeId, transactionRequest.typeId) &&
        Objects.equals(this.subAccountId, transactionRequest.subAccountId) &&
        Objects.equals(this.amount, transactionRequest.amount) &&
        Objects.equals(this.remarks, transactionRequest.remarks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(time, typeId, subAccountId, amount, remarks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionRequest {\n");
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
    sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
    sb.append("    subAccountId: ").append(toIndentedString(subAccountId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
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

