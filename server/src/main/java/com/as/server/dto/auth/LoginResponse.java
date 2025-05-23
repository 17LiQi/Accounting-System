package com.as.server.dto.auth;

import com.as.server.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * LoginResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-07T20:09:16.831236500+08:00[Asia/Shanghai]")
public class LoginResponse   {

  @JsonProperty("token")
  private String token;



  @JsonProperty("role")
  private Role role;

  public LoginResponse token(String token) {
    this.token = token;
    return this;
  }

  /**
   * JWT 令牌
   * @return token
  */
  @NotNull 
  @Schema(name = "token", description = "JWT 令牌", required = true)
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public LoginResponse role(Role role) {
    this.role = role;
    return this;
  }

  /**
   * 用户角色
   * @return role
  */
  @NotNull 
  @Schema(name = "role", description = "用户角色", required = true)
  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginResponse loginResponse = (LoginResponse) o;
    return Objects.equals(this.token, loginResponse.token) &&
        Objects.equals(this.role, loginResponse.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, role);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginResponse {\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
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

