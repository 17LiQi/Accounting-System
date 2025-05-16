package com.as.server.dto.statistics;

import com.as.server.enums.Period;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * StatisticsResponse
 */
@Setter
public class StatisticsResponse {

  @JsonProperty("period")
  private Period period;

  @JsonProperty("year")
  private Integer year;

  @JsonProperty("month")
  private Integer month;

  @JsonProperty("week")
  private Integer week;

  @JsonProperty("day")
  private Integer day;

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

  public StatisticsResponse period(Period period) {
    this.period = period;
    return this;
  }

  /**
   * 统计周期：daily, weekly, monthly, yearly
   * @return period
   */
  @NotNull
  @Schema(name = "period", description = "统计周期：daily, weekly, monthly, yearly", required = true)
  public Period getPeriod() {
    return period;
  }

    public StatisticsResponse year(Integer year) {
    this.year = year;
    return this;
  }

  /**
   * 年份
   * minimum: 2000
   * maximum: 2100
   * @return year
   */
  @NotNull
  @Min(2000)
  @Max(2100)
  @Schema(name = "year", description = "年份", required = true)
  public Integer getYear() {
    return year;
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
  @Min(1)
  @Max(12)
  @Schema(name = "month", description = "月份（月度统计时需要）", required = false)
  public Integer getMonth() {
    return month;
  }

    public StatisticsResponse week(Integer week) {
    this.week = week;
    return this;
  }

  /**
   * 周次（周度统计时需要）
   * minimum: 1
   * maximum: 53
   * @return week
   */
  @Min(1)
  @Max(53)
  @Schema(name = "week", description = "周次（周度统计时需要）")
  public Integer getWeek() {
    return week;
  }

    public StatisticsResponse day(Integer day) {
    this.day = day;
    return this;
  }

  /**
   * 日期（日度统计时需要）
   * minimum: 1
   * maximum: 31
   * @return day
   */
  @Min(1)
  @Max(31)
  @Schema(name = "day", description = "日期（日度统计时需要）")
  public Integer getDay() {
    return day;
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
  @Schema(name = "incomeByType", description = "按类型统计的收入")
  public List<StatisticsResponseIncomeByType> getIncomeByType() {
    return incomeByType;
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
  @Schema(name = "expenseByType", description = "按类型统计的支出")
  public List<StatisticsResponseExpenseByType> getExpenseByType() {
    return expenseByType;
  }

    public StatisticsResponse totalIncome(String totalIncome) {
    this.totalIncome = totalIncome;
    return this;
  }

  /**
   * 总收入（固定 2 位小数）
   * @return totalIncome
   */
  @NotNull
  @Pattern(regexp = "^\\d+(\\.\\d{2})?$")
  @Schema(name = "totalIncome", description = "总收入（固定 2 位小数）", required = true)
  public String getTotalIncome() {
    return totalIncome;
  }

    public StatisticsResponse totalExpense(String totalExpense) {
    this.totalExpense = totalExpense;
    return this;
  }

  /**
   * 总支出（固定 2 位小数）
   * @return totalExpense
   */
  @NotNull
  @Pattern(regexp = "^\\d+(\\.\\d{2})?$")
  @Schema(name = "totalExpense", description = "总支出（固定 2 位小数）", required = true)
  public String getTotalExpense() {
    return totalExpense;
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
            Objects.equals(this.week, statisticsResponse.week) &&
            Objects.equals(this.day, statisticsResponse.day) &&
            Objects.equals(this.incomeByType, statisticsResponse.incomeByType) &&
            Objects.equals(this.expenseByType, statisticsResponse.expenseByType) &&
            Objects.equals(this.totalIncome, statisticsResponse.totalIncome) &&
            Objects.equals(this.totalExpense, statisticsResponse.totalExpense);
  }

  @Override
  public int hashCode() {
    return Objects.hash(period, year, month, week, day, incomeByType, expenseByType, totalIncome, totalExpense);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatisticsResponse {\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    month: ").append(toIndentedString(month)).append("\n");
    sb.append("    week: ").append(toIndentedString(week)).append("\n");
    sb.append("    day: ").append(toIndentedString(day)).append("\n");
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