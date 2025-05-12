package com.as.server.dto.accounts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * SubAccount
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-07T20:09:16.831236500+08:00[Asia/Shanghai]")
public class SubAccountDTO {

  @JsonProperty("subAccountId")
  private Integer subAccountId;

  @JsonProperty("accountId")
  private Integer accountId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("accountNumber")
  private String accountNumber;

  /**
   * Gets or Sets cardType
   */
  public enum CardTypeEnum {
    SAVINGS("SAVINGS"),
    
    DEBIT("DEBIT"),
    
    CREDIT("CREDIT"),
    
    WALLET("WALLET");

    private String value;

    CardTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CardTypeEnum fromValue(String value) {
      for (CardTypeEnum b : CardTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @JsonProperty("cardType")
  private CardTypeEnum cardType;

  @JsonProperty("balance")
  private String balance;

  public SubAccountDTO subAccountId(Integer subAccountId) {
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

  public SubAccountDTO accountId(Integer accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
  */
  @NotNull 
  @Schema(name = "accountId", required = true)
  public Integer getAccountId() {
    return accountId;
  }

  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }

  public SubAccountDTO name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  @NotNull @Size(max = 100) 
  @Schema(name = "name", required = true)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SubAccountDTO accountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
    return this;
  }

  /**
   * Get accountNumber
   * @return accountNumber
  */
  @NotNull @Size(max = 50) 
  @Schema(name = "accountNumber", required = true)
  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public SubAccountDTO cardType(CardTypeEnum cardType) {
    this.cardType = cardType;
    return this;
  }

  /**
   * Get cardType
   * @return cardType
  */
  @NotNull 
  @Schema(name = "cardType", required = true)
  public CardTypeEnum getCardType() {
    return cardType;
  }

  public void setCardType(CardTypeEnum cardType) {
    this.cardType = cardType;
  }

  public SubAccountDTO balance(String balance) {
    this.balance = balance;
    return this;
  }

  /**
   * Get balance
   * @return balance
  */
  @NotNull @Pattern(regexp = "^\\d+(\\.\\d{2})?$") 
  @Schema(name = "balance", required = true)
  public String getBalance() {
    return balance;
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubAccountDTO subAccount = (SubAccountDTO) o;
    return Objects.equals(this.subAccountId, subAccount.subAccountId) &&
        Objects.equals(this.accountId, subAccount.accountId) &&
        Objects.equals(this.name, subAccount.name) &&
        Objects.equals(this.accountNumber, subAccount.accountNumber) &&
        Objects.equals(this.cardType, subAccount.cardType) &&
        Objects.equals(this.balance, subAccount.balance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subAccountId, accountId, name, accountNumber, cardType, balance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubAccount {\n");
    sb.append("    subAccountId: ").append(toIndentedString(subAccountId)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
    sb.append("    cardType: ").append(toIndentedString(cardType)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
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

