# StatisticsResponse


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**period** | **string** | 统计周期（月度或年度） | [default to undefined]
**year** | **number** | 年份 | [default to undefined]
**month** | **number** | 月份（月度统计时需要） | [optional] [default to undefined]
**incomeByType** | [**Array&lt;StatisticsResponseIncomeByType&gt;**](StatisticsResponseIncomeByType.md) | 按类型统计的收入 | [optional] [default to undefined]
**expenseByType** | [**Array&lt;StatisticsResponseExpenseByType&gt;**](StatisticsResponseExpenseByType.md) | 按类型统计的支出 | [optional] [default to undefined]
**totalIncome** | **string** | 总收入（固定 2 位小数） | [default to undefined]
**totalExpense** | **string** | 总支出（固定 2 位小数） | [default to undefined]

## Example

```typescript
import { StatisticsResponse } from './api';

const instance: StatisticsResponse = {
    period,
    year,
    month,
    incomeByType,
    expenseByType,
    totalIncome,
    totalExpense,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
