package com.as.server.dto.users;

import com.as.server.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * User
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-07T20:09:16.831236500+08:00[Asia/Shanghai]")
public class UserDTO {

  @JsonProperty("userId")
  private Integer userId;

  @JsonProperty("username")
  private String username;

  @JsonProperty("role")
  private Role role;

  public UserDTO userId(Integer userId) {
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

  public UserDTO username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
  */
  @NotNull @Size(max = 50) 
  @Schema(name = "username", required = true)
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserDTO role(Role role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
  */
  @NotNull 
  @Schema(name = "role", required = true)
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
    UserDTO user = (UserDTO) o;
    return Objects.equals(this.userId, user.userId) &&
        Objects.equals(this.username, user.username) &&
        Objects.equals(this.role, user.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, username, role);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
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

