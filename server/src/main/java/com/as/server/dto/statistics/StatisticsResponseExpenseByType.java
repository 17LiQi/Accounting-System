package com.as.server.dto.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * StatisticsResponseExpenseByType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-07T20:30:49.533631100+08:00[Asia/Shanghai]")
public class StatisticsResponseExpenseByType   {

  @JsonProperty("typeId")
  private Integer typeId;

  @JsonProperty("typeName")
  private String typeName;

  @JsonProperty("amount")
  private String amount;

  public StatisticsResponseExpenseByType typeId(Integer typeId) {
    this.typeId = typeId;
    return this;
  }

  public StatisticsResponseExpenseByType() {
  }

  public StatisticsResponseExpenseByType(Integer typeId, String typeName, BigDecimal amount) {
    this.typeId = typeId;
    this.typeName = typeName;
    this.amount = amount != null ? amount.setScale(2, RoundingMode.HALF_UP).toString() : "0.00";
  }
  /**
   * 交易类型 ID
   * @return typeId
  */
  @NotNull 
  @Schema(name = "typeId", description = "交易类型 ID", required = true)
  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public StatisticsResponseExpenseByType typeName(String typeName) {
    this.typeName = typeName;
    return this;
  }

  /**
   * 交易类型名称（如“日常消费”）
   * @return typeName
  */
  @NotNull @Size(max = 50) 
  @Schema(name = "typeName", description = "交易类型名称（如“日常消费”）", required = true)
  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public StatisticsResponseExpenseByType amount(String amount) {
    this.amount = amount;
    return this;
  }

  /**
   * 支出金额（固定 2 位小数）
   * @return amount
  */
  @NotNull @Pattern(regexp = "^\\d+(\\.\\d{2})?$") 
  @Schema(name = "amount", description = "支出金额（固定 2 位小数）", required = true)
  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatisticsResponseExpenseByType statisticsResponseExpenseByType = (StatisticsResponseExpenseByType) o;
    return Objects.equals(this.typeId, statisticsResponseExpenseByType.typeId) &&
        Objects.equals(this.typeName, statisticsResponseExpenseByType.typeName) &&
        Objects.equals(this.amount, statisticsResponseExpenseByType.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(typeId, typeName, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatisticsResponseExpenseByType {\n");
    sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
    sb.append("    typeName: ").append(toIndentedString(typeName)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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

