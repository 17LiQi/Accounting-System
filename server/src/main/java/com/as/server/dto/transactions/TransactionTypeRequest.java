package com.as.server.dto.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * TransactionTypeRequest
 */
@Setter
public class TransactionTypeRequest {

    @JsonProperty("typeName")
    private String typeName;

    @JsonProperty("isIncome")
    private Boolean isIncome;

    public TransactionTypeRequest typeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    /**
     * 获取交易类型名称
     * @return typeName
     */
    @NotNull
    @Size(max = 50)
    @Schema(name = "typeName", description = "交易类型名称", required = true)
    public String getTypeName() {
        return typeName;
    }

    public TransactionTypeRequest isIncome(Boolean isIncome) {
        this.isIncome = isIncome;
        return this;
    }

    /**
     * 是否为收入
     * @return isIncome
     */
    @NotNull
    @Schema(name = "isIncome", description = "是否为收入，TRUE 为收入，FALSE 为支出", required = true)
    public Boolean getIsIncome() {
        return isIncome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransactionTypeRequest request = (TransactionTypeRequest) o;
        return Objects.equals(this.typeName, request.typeName) &&
                Objects.equals(this.isIncome, request.isIncome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, isIncome);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TransactionTypeRequest {\n");
        sb.append("    typeName: ").append(toIndentedString(typeName)).append("\n");
        sb.append("    isIncome: ").append(toIndentedString(isIncome)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * 将对象转换为带缩进的字符串，每行缩进4个空格（首行除外）。
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}