# Transaction


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**transactionId** | **number** |  | [optional] [default to undefined]
**time** | **string** |  | [optional] [default to undefined]
**transactionType** | [**TransactionType**](TransactionType.md) |  | [optional] [default to undefined]
**subAccount** | [**SubAccount**](SubAccount.md) |  | [optional] [default to undefined]
**amount** | **number** |  | [optional] [default to undefined]
**user** | [**User**](User.md) |  | [optional] [default to undefined]
**remarks** | **string** |  | [optional] [default to undefined]

## Example

```typescript
import { Transaction } from './api';

const instance: Transaction = {
    transactionId,
    time,
    transactionType,
    subAccount,
    amount,
    user,
    remarks,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
