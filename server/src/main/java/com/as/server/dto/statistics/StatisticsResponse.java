package com.as.server.dto.statistics;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import javax.annotation.Generated;

/**
 * StatisticsResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-07T20:09:16.831236500+08:00[Asia/Shanghai]")
public class StatisticsResponse   {

  /**
   * 统计周期（月度或年度）
   */
  public enum PeriodEnum {
    MONTHLY("monthly"),
    
    ANNUAL("annual");

    private String value;

    PeriodEnum(String value) {
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
    public static PeriodEnum fromValue(String value) {
      for (PeriodEnum b : PeriodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @JsonProperty("period")
  private PeriodEnum period;

  @JsonProperty("year")
  private Integer year;

  @JsonProperty("month")
  private Integer month;

  @JsonProperty("incomeByType")
  @Valid
  private List<StatisticsResponseIncomeByType> incomeByType = null;

  @JsonProperty("expenseByType")
  @Valid
  private List<StatisticsResponseExpenseByType> expenseByType = null;

  @JsonProperty("totalIncome")
  private String totalIncome;

  @JsonProperty("totalExpense")
  private String totalExpense;

  public StatisticsResponse period(PeriodEnum period) {
    this.period = period;
    return this;
  }

  /**
   * 统计周期（月度或年度）
   * @return period
  */
  @NotNull 
  @Schema(name = "period", description = "统计周期（月度或年度）", required = true)
  public PeriodEnum getPeriod() {
    return period;
  }

  public void setPeriod(PeriodEnum period) {
    this.period = period;
  }

  public StatisticsResponse year(Integer year) {
    this.year = year;
    return this;
  }

  /**
   * 年份
   * @return year
  */
  @NotNull 
  @Schema(name = "year", description = "年份", required = true)
  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public StatisticsResponse month(Integer month) {
    this.month = month;
    return this;
  }

  /**
   * 月份（月度统计时需要）
   * minimum: 1
   * maximum: 12
   * @return month
  */
  @Min(1) @Max(12) 
  @Schema(name = "month", description = "月份（月度统计时需要）", required = false)
  public Integer getMonth() {
    return month;
  }

  public void setMonth(Integer month) {
    this.month = month;
  }

  public StatisticsResponse incomeByType(List<StatisticsResponseIncomeByType> incomeByType) {
    this.incomeByType = incomeByType;
    return this;
  }

  public StatisticsResponse addIncomeByTypeItem(StatisticsResponseIncomeByType incomeByTypeItem) {
    if (this.incomeByType == null) {
      this.incomeByType = new ArrayList<>();
    }
    this.incomeByType.add(incomeByTypeItem);
    return this;
  }

  /**
   * 按类型统计的收入
   * @return incomeByType
  */
  @Valid 
  @Schema(name = "incomeByType", description = "按类型统计的收入", required = false)
  public List<StatisticsResponseIncomeByType> getIncomeByType() {
    return incomeByType;
  }

  public void setIncomeByType(List<StatisticsResponseIncomeByType> incomeByType) {
    this.incomeByType = incomeByType;
  }

  public StatisticsResponse expenseByType(List<StatisticsResponseExpenseByType> expenseByType) {
    this.expenseByType = expenseByType;
    return this;
  }

  public StatisticsResponse addExpenseByTypeItem(StatisticsResponseExpenseByType expenseByTypeItem) {
    if (this.expenseByType == null) {
      this.expenseByType = new ArrayList<>();
    }
    this.expenseByType.add(expenseByTypeItem);
    return this;
  }

  /**
   * 按类型统计的支出
   * @return expenseByType
  */
  @Valid 
  @Schema(name = "expenseByType", description = "按类型统计的支出", required = false)
  public List<StatisticsResponseExpenseByType> getExpenseByType() {
    return expenseByType;
  }

  public void setExpenseByType(List<StatisticsResponseExpenseByType> expenseByType) {
    this.expenseByType = expenseByType;
  }

  public StatisticsResponse totalIncome(String totalIncome) {
    this.totalIncome = totalIncome;
    return this;
  }

  /**
   * 总收入（固定 2 位小数）
   * @return totalIncome
  */
  @NotNull @Pattern(regexp = "^\\d+(\\.\\d{2})?$") 
  @Schema(name = "totalIncome", description = "总收入（固定 2 位小数）", required = true)
  public String getTotalIncome() {
    return totalIncome;
  }

  public void setTotalIncome(String totalIncome) {
    this.totalIncome = totalIncome;
  }

  public StatisticsResponse totalExpense(String totalExpense) {
    this.totalExpense = totalExpense;
    return this;
  }

  /**
   * 总支出（固定 2 位小数）
   * @return totalExpense
  */
  @NotNull @Pattern(regexp = "^\\d+(\\.\\d{2})?$") 
  @Schema(name = "totalExpense", description = "总支出（固定 2 位小数）", required = true)
  public String getTotalExpense() {
    return totalExpense;
  }

  public void setTotalExpense(String totalExpense) {
    this.totalExpense = totalExpense;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatisticsResponse statisticsResponse = (StatisticsResponse) o;
    return Objects.equals(this.period, statisticsResponse.period) &&
        Objects.equals(this.year, statisticsResponse.year) &&
        Objects.equals(this.month, statisticsResponse.month) &&
        Objects.equals(this.incomeByType, statisticsResponse.incomeByType) &&
        Objects.equals(this.expenseByType, statisticsResponse.expenseByType) &&
        Objects.equals(this.totalIncome, statisticsResponse.totalIncome) &&
        Objects.equals(this.totalExpense, statisticsResponse.totalExpense);
  }

  @Override
  public int hashCode() {
    return Objects.hash(period, year, month, incomeByType, expenseByType, totalIncome, totalExpense);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatisticsResponse {\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    month: ").append(toIndentedString(month)).append("\n");
    sb.append("    incomeByType: ").append(toIndentedString(incomeByType)).append("\n");
    sb.append("    expenseByType: ").append(toIndentedString(expenseByType)).append("\n");
    sb.append("    totalIncome: ").append(toIndentedString(totalIncome)).append("\n");
    sb.append("    totalExpense: ").append(toIndentedString(totalExpense)).append("\n");
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

