package com.as.server.dto.accounts;

import com.as.server.enums.CardType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

/**
 * SubAccountRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-07T20:09:16.831236500+08:00[Asia/Shanghai]")
@Schema(description = "子账户创建/更新请求")
public class SubAccountRequest   {

  @JsonProperty("accountId")
  @Schema(description = "账户ID", required = true)
  @NotNull(message = "账户ID不能为空")
  private Integer accountId;

  @JsonProperty("accountName")
  @Schema(description = "子账户名称", required = true)
  @NotNull @Size(max = 100) 
  private String accountName;

  @JsonProperty("accountNumber")
  @Schema(description = "子账户编号", required = true)
  @NotNull @Size(max = 50) 
  private String accountNumber;

  @JsonProperty("cardType")
  @Schema(description = "卡类型", required = true)
  @NotNull 
  private CardType cardType;

  @JsonProperty("balance")
  @Schema(description = "余额", required = true)
  @NotNull @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "余额必须是数字，最多两位小数")
  private String balance;

  @JsonProperty("userIds")
  @Schema(description = "关联用户ID列表")
  private List<Integer> userIds;

  public SubAccountRequest accountId(Integer accountId) {
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

  public SubAccountRequest accountName(String accountName) {
    this.accountName = accountName;
    return this;
  }

  /**
   * Get accountName
   * @return accountName
  */
  @NotNull @Size(max = 100) 
  @Schema(name = "accountName", required = true)
  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public SubAccountRequest accountNumber(String accountNumber) {
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

  public SubAccountRequest cardType(CardType cardType) {
    this.cardType = cardType;
    return this;
  }

  /**
   * Get cardType
   * @return cardType
  */
  @NotNull 
  @Schema(name = "cardType", required = true)
  public CardType getCardType() {
    return cardType;
  }

  public void setCardType(CardType cardType) {
    this.cardType = cardType;
  }

  public SubAccountRequest balance(String balance) {
    this.balance = balance;
    return this;
  }

  /**
   * Get balance
   * @return balance
  */
  @NotNull @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$") 
  @Schema(name = "balance", required = true)
  public String getBalance() {
    return balance;
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }

  public List<Integer> getUserIds() {
    return userIds;
  }

  public void setUserIds(List<Integer> userIds) {
    this.userIds = userIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubAccountRequest subAccountRequest = (SubAccountRequest) o;
    return Objects.equals(this.accountId, subAccountRequest.accountId) &&
        Objects.equals(this.accountName, subAccountRequest.accountName) &&
        Objects.equals(this.accountNumber, subAccountRequest.accountNumber) &&
        Objects.equals(this.cardType, subAccountRequest.cardType) &&
        Objects.equals(this.balance, subAccountRequest.balance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, accountName, accountNumber, cardType, balance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubAccountRequest {\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    accountName: ").append(toIndentedString(accountName)).append("\n");
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

