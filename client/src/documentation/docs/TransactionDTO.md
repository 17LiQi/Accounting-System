# TransactionDTO


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**transactionId** | **number** |  | [default to undefined]
**time** | **string** |  | [default to undefined]
**typeId** | **number** |  | [default to undefined]
**isIncome** | **boolean** | 是否为收入（由typeId关联的transaction_types.is_income自动生成） | [optional] [default to undefined]
**subAccountId** | **number** |  | [default to undefined]
**amount** | **string** |  | [default to undefined]
**userId** | **number** |  | [default to undefined]
**remarks** | **string** |  | [optional] [default to undefined]

## Example

```typescript
import { TransactionDTO } from './api';

const instance: TransactionDTO = {
    transactionId,
    time,
    typeId,
    isIncome,
    subAccountId,
    amount,
    userId,
    remarks,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
