# TransactionsApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**transactionsCreate**](#transactionscreate) | **POST** /transactions | 创建交易记录|
|[**transactionsList**](#transactionslist) | **GET** /transactions | 列出交易记录|

# **transactionsCreate**
> Transaction transactionsCreate()

创建新的交易记录。普通用户仅可为自己的子账户创建记录（匹配 user_sub_accounts）。 管理员可为任何用户创建。isIncome 由 typeId 对应的 transaction_types.is_income 确定。 

### Example

```typescript
import {
    TransactionsApi,
    Configuration,
    TransactionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionsApi(configuration);

let transactionRequest: TransactionRequest; // (optional)

const { status, data } = await apiInstance.transactionsCreate(
    transactionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **transactionRequest** | **TransactionRequest**|  | |


### Return type

**Transaction**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | 交易记录已创建 |  -  |
|**400** | 无效输入（交易类型不存在或参数格式错误） |  -  |
|**403** | 权限不足（普通用户尝试访问未关联的子账户） |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **transactionsList**
> TransactionListResponse transactionsList()

获取交易记录列表。普通用户仅可查看自己的记录（匹配 JWT 中的 userId）。 管理员可按 userId 过滤。 

### Example

```typescript
import {
    TransactionsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionsApi(configuration);

let page: number; // (default to undefined)
let size: number; // (default to undefined)
let userId: number; // (optional) (default to undefined)
let subAccountId: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.transactionsList(
    page,
    size,
    userId,
    subAccountId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **page** | [**number**] |  | defaults to undefined|
| **size** | [**number**] |  | defaults to undefined|
| **userId** | [**number**] |  | (optional) defaults to undefined|
| **subAccountId** | [**number**] |  | (optional) defaults to undefined|


### Return type

**TransactionListResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 交易记录列表 |  -  |
|**403** | 权限不足（普通用户尝试访问未关联的数据） |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

