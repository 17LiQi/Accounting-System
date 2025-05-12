package com.as.server.dto.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * TransactionType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-07T20:30:49.533631100+08:00[Asia/Shanghai]")
public class TransactionTypeDTO {

  @JsonProperty("typeId")
  private Integer typeId;

  @JsonProperty("typeName")
  private String typeName;

  @JsonProperty("isIncome")
  private Boolean isIncome;

  public TransactionTypeDTO typeId(Integer typeId) {
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

  public TransactionTypeDTO typeName(String typeName) {
    this.typeName = typeName;
    return this;
  }

  /**
   * Get typeName
   * @return typeName
  */
  @NotNull @Size(max = 50) 
  @Schema(name = "typeName", required = true)
  public String gettypeName() {
    return typeName;
  }

  public void settypeName(String typeName) {
    this.typeName = typeName;
  }

  public TransactionTypeDTO isIncome(Boolean isIncome) {
    this.isIncome = isIncome;
    return this;
  }

  /**
   * Get isIncome
   * @return isIncome
  */
  @NotNull 
  @Schema(name = "isIncome", required = true)
  public Boolean getIsIncome() {
    return isIncome;
  }

  public void setIsIncome(Boolean isIncome) {
    this.isIncome = isIncome;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionTypeDTO transactionType = (TransactionTypeDTO) o;
    return Objects.equals(this.typeId, transactionType.typeId) &&
        Objects.equals(this.typeName, transactionType.typeName) &&
        Objects.equals(this.isIncome, transactionType.isIncome);
  }

  @Override
  public int hashCode() {
    return Objects.hash(typeId, typeName, isIncome);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionType {\n");
    sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
    sb.append("    typeName: ").append(toIndentedString(typeName)).append("\n");
    sb.append("    isIncome: ").append(toIndentedString(isIncome)).append("\n");
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

