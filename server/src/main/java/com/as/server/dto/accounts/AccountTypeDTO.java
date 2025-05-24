package com.as.server.dto.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-07T20:09:16.831236500+08:00[Asia/Shanghai]")
public class AccountTypeDTO {

    @JsonProperty("typeId")
    private Integer typeId;

    @JsonProperty("typeName")
    private String typeName;

    public AccountTypeDTO typeId(Integer typeId) {
        this.typeId = typeId;
        return this;
    }

    @NotNull
    @Schema(name = "typeId", description = "账户类型 ID", required = true)
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public AccountTypeDTO typeName(String typeName) {
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
        AccountTypeDTO accountTypeDTO = (AccountTypeDTO) o;
        return Objects.equals(this.typeId, accountTypeDTO.typeId) &&
                Objects.equals(this.typeName, accountTypeDTO.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeId, typeName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AccountTypeDTO {\n");
        sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
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