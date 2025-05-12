# TransactionTypesApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**transactionTypesCreate**](#transactiontypescreate) | **POST** /transaction-types | 创建交易类型|
|[**transactionTypesDelete**](#transactiontypesdelete) | **DELETE** /transaction-types/{typeId} | 删除交易类型|
|[**transactionTypesList**](#transactiontypeslist) | **GET** /transaction-types | 列出交易类型|
|[**transactionTypesUpdate**](#transactiontypesupdate) | **PUT** /transaction-types/{typeId} | 更新交易类型|

# **transactionTypesCreate**
> TransactionType transactionTypesCreate()

管理员创建新的交易类型（收入或支出）。

### Example

```typescript
import {
    TransactionTypesApi,
    Configuration,
    TransactionType
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionTypesApi(configuration);

let transactionType: TransactionType; // (optional)

const { status, data } = await apiInstance.transactionTypesCreate(
    transactionType
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **transactionType** | **TransactionType**|  | |


### Return type

**TransactionType**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | 交易类型已创建 |  -  |
|**400** | 无效输入（名称重复或参数错误） |  -  |
|**403** | 权限不足（非管理员） |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **transactionTypesDelete**
> transactionTypesDelete()

管理员删除指定交易类型，需确保无关联交易记录。

### Example

```typescript
import {
    TransactionTypesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionTypesApi(configuration);

let typeId: number; //交易类型 ID (default to undefined)

const { status, data } = await apiInstance.transactionTypesDelete(
    typeId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **typeId** | [**number**] | 交易类型 ID | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**204** | 交易类型已删除 |  -  |
|**403** | 权限不足（非管理员） |  -  |
|**404** | 交易类型不存在 |  -  |
|**409** | 交易类型存在关联交易记录，无法删除 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **transactionTypesList**
> Array<TransactionType> transactionTypesList()

获取所有交易类型（收入/支出），用于新增交易时选择类型。

### Example

```typescript
import {
    TransactionTypesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionTypesApi(configuration);

const { status, data } = await apiInstance.transactionTypesList();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<TransactionType>**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 交易类型列表 |  -  |
|**403** | 权限不足 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **transactionTypesUpdate**
> TransactionType transactionTypesUpdate()

管理员更新指定交易类型的名称或收入/支出属性。

### Example

```typescript
import {
    TransactionTypesApi,
    Configuration,
    TransactionType
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionTypesApi(configuration);

let typeId: number; //交易类型 ID (default to undefined)
let transactionType: TransactionType; // (optional)

const { status, data } = await apiInstance.transactionTypesUpdate(
    typeId,
    transactionType
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **transactionType** | **TransactionType**|  | |
| **typeId** | [**number**] | 交易类型 ID | defaults to undefined|


### Return type

**TransactionType**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 交易类型已更新 |  -  |
|**400** | 无效输入（名称重复或参数错误） |  -  |
|**403** | 权限不足（非管理员） |  -  |
|**404** | 交易类型不存在 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

