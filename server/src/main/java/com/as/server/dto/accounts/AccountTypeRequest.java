package com.as.server.dto.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-07T20:09:16.831236500+08:00[Asia/Shanghai]")
public class AccountTypeRequest {

    @JsonProperty("typeName")
    private String typeName;

    public AccountTypeRequest typeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    @NotNull
    @Size(max = 50, min = 1)
    @Schema(name = "typeName", description = "账户类型名称", required = true)
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountTypeRequest accountTypeRequest = (AccountTypeRequest) o;
        return Objects.equals(this.typeName, accountTypeRequest.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AccountTypeRequest {\n");
        sb.append("    typeName: ").append(toIndentedString(typeName)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
} 