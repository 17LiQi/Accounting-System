package com.as.server.dto.statistics;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import javax.annotation.Generated;

/**
 * StatisticsResponseIncomeByType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-07T20:30:49.533631100+08:00[Asia/Shanghai]")
public class StatisticsResponseIncomeByType   {

  @JsonProperty("typeId")
  private Integer typeId;

  @JsonProperty("typeName")
  private String typeName;

  @JsonProperty("amount")
  private String amount;

  public StatisticsResponseIncomeByType typeId(Integer typeId) {
    this.typeId = typeId;
    return this;
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

  public StatisticsResponseIncomeByType typeName(String typeName) {
    this.typeName = typeName;
    return this;
  }

  /**
   * 交易类型名称（如“工资”）
   * @return typeName
  */
  @NotNull @Size(max = 50) 
  @Schema(name = "typeName", description = "交易类型名称（如“工资”）", required = true)
  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public StatisticsResponseIncomeByType amount(String amount) {
    this.amount = amount;
    return this;
  }

  /**
   * 收入金额（固定 2 位小数）
   * @return amount
  */
  @NotNull @Pattern(regexp = "^\\d+(\\.\\d{2})?$") 
  @Schema(name = "amount", description = "收入金额（固定 2 位小数）", required = true)
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
    StatisticsResponseIncomeByType statisticsResponseIncomeByType = (StatisticsResponseIncomeByType) o;
    return Objects.equals(this.typeId, statisticsResponseIncomeByType.typeId) &&
        Objects.equals(this.typeName, statisticsResponseIncomeByType.typeName) &&
        Objects.equals(this.amount, statisticsResponseIncomeByType.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(typeId, typeName, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatisticsResponseIncomeByType {\n");
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

