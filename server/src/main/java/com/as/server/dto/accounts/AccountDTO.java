package com.as.server.dto.accounts;

import com.as.server.enums.AccountType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Account
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-07T20:09:16.831236500+08:00[Asia/Shanghai]")
public class AccountDTO {

  @JsonProperty("accountId")
  private Integer accountId;

  @JsonProperty("accountName")
  private String accountName;

  @JsonProperty("typeId")
  private Integer typeId;

  @JsonProperty("type")
  private AccountType type;

  public AccountDTO accountId(Integer accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * 顶级账户 ID
   * @return accountId
   */
  @NotNull
  @Schema(name = "accountId", description = "顶级账户 ID", required = true)
  public Integer getAccountId() {
    return accountId;
  }

  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }

  public AccountDTO accountName(String accountName) {
    this.accountName = accountName;
    return this;
  }

  /**
   * 账户名称（如"我的银行账户"）
   * @return accountName
   */
  @NotNull
  @Size(max = 100)
  @Schema(name = "accountName", description = "账户名称（如\"我的银行账户\"）", required = true)
  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public AccountDTO typeId(Integer typeId) {
    this.typeId = typeId;
    return this;
  }

  /**
   * 账户类型 ID，对应 account_types.type_id
   * @return typeId
   */
  @NotNull
  @Schema(name = "typeId", description = "账户类型 ID，对应 account_types.type_id", required = true)
  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public AccountDTO type(AccountType type) {
    this.type = type;
    return this;
  }

  /**
   * 账户类型
   * @return type
   */
  @NotNull
  @Schema(name = "type", description = "账户类型", required = true)
  public AccountType getType() {
    return type;
  }

  public void setType(AccountType type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountDTO account = (AccountDTO) o;
    return Objects.equals(this.accountId, account.accountId) &&
            Objects.equals(this.accountName, account.accountName) &&
            Objects.equals(this.typeId, account.typeId) &&
            Objects.equals(this.type, account.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, accountName, typeId, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountDTO {\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    accountName: ").append(toIndentedString(accountName)).append("\n");
    sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

