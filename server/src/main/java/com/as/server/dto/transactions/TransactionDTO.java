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
 * Transaction
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-07T20:09:16.831236500+08:00[Asia/Shanghai]")
public class TransactionDTO {

  @JsonProperty("transactionId")
  private Integer transactionId;

  @JsonProperty("time")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime time;

  @JsonProperty("typeId")
  private Integer typeId;

  @JsonProperty("isIncome")
  private Boolean isIncome;

  @JsonProperty("subAccountId")
  private Integer subAccountId;

  @JsonProperty("amount")
  private String amount;

  @JsonProperty("userId")
  private Integer userId;

  @JsonProperty("remarks")
  private String remarks;

  public TransactionDTO transactionId(Integer transactionId) {
    this.transactionId = transactionId;
    return this;
  }

  /**
   * Get transactionId
   * @return transactionId
  */
  @NotNull 
  @Schema(name = "transactionId", required = true)
  public Integer getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(Integer transactionId) {
    this.transactionId = transactionId;
  }

  public TransactionDTO time(OffsetDateTime time) {
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

  public TransactionDTO typeId(Integer typeId) {
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

  public TransactionDTO isIncome(Boolean isIncome) {
    this.isIncome = isIncome;
    return this;
  }

  /**
   * 是否为收入（由typeId关联的transaction_types.is_income自动生成）
   * @return isIncome
  */
  
  @Schema(name = "isIncome", description = "是否为收入（由typeId关联的transaction_types.is_income自动生成）", required = false)
  public Boolean getIsIncome() {
    return isIncome;
  }

  public void setIsIncome(Boolean isIncome) {
    this.isIncome = isIncome;
  }

  public TransactionDTO subAccountId(Integer subAccountId) {
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

  public TransactionDTO amount(String amount) {
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

  public TransactionDTO userId(Integer userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
  */
  @NotNull 
  @Schema(name = "userId", required = true)
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public TransactionDTO remarks(String remarks) {
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
    TransactionDTO transaction = (TransactionDTO) o;
    return Objects.equals(this.transactionId, transaction.transactionId) &&
        Objects.equals(this.time, transaction.time) &&
        Objects.equals(this.typeId, transaction.typeId) &&
        Objects.equals(this.isIncome, transaction.isIncome) &&
        Objects.equals(this.subAccountId, transaction.subAccountId) &&
        Objects.equals(this.amount, transaction.amount) &&
        Objects.equals(this.userId, transaction.userId) &&
        Objects.equals(this.remarks, transaction.remarks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionId, time, typeId, isIncome, subAccountId, amount, userId, remarks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Transaction {\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
    sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
    sb.append("    isIncome: ").append(toIndentedString(isIncome)).append("\n");
    sb.append("    subAccountId: ").append(toIndentedString(subAccountId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
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

