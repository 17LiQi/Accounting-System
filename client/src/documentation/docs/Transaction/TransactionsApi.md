# TransactionsApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteTransaction**](#deletetransaction) | **DELETE** /transactions/{id} | 删除交易记录|
|[**deleteTransaction_0**](#deletetransaction_0) | **DELETE** /transactions/{id} | 删除交易记录|
|[**getTransaction**](#gettransaction) | **GET** /transactions/{id} | 获取交易记录详情|
|[**getTransaction_0**](#gettransaction_0) | **GET** /transactions/{id} | 获取交易记录详情|
|[**transactionsCreate**](#transactionscreate) | **POST** /transactions | 创建交易记录|
|[**transactionsCreate_0**](#transactionscreate_0) | **POST** /transactions | 创建交易记录|
|[**transactionsList**](#transactionslist) | **GET** /transactions | 列出交易记录|
|[**transactionsList_0**](#transactionslist_0) | **GET** /transactions | 列出交易记录|
|[**updateTransaction**](#updatetransaction) | **PUT** /transactions/{id} | 更新交易记录|
|[**updateTransaction_0**](#updatetransaction_0) | **PUT** /transactions/{id} | 更新交易记录|

# **deleteTransaction**
> deleteTransaction()


### Example

```typescript
import {
    TransactionsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionsApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.deleteTransaction(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**204** | 交易记录已删除 |  -  |
|**403** | 权限不足（普通用户尝试访问未关联的数据） |  -  |
|**404** | 交易记录不存在 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deleteTransaction_0**
> deleteTransaction_0()


### Example

```typescript
import {
    TransactionsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionsApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.deleteTransaction_0(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**204** | 交易记录已删除 |  -  |
|**403** | 权限不足（普通用户尝试访问未关联的数据） |  -  |
|**404** | 交易记录不存在 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getTransaction**
> TransactionDTO getTransaction()


### Example

```typescript
import {
    TransactionsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionsApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.getTransaction(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**TransactionDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 交易记录详情 |  -  |
|**403** | 权限不足（普通用户尝试访问未关联的数据） |  -  |
|**404** | 交易记录不存在 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getTransaction_0**
> TransactionDTO getTransaction_0()


### Example

```typescript
import {
    TransactionsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionsApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.getTransaction_0(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**TransactionDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 交易记录详情 |  -  |
|**403** | 权限不足（普通用户尝试访问未关联的数据） |  -  |
|**404** | 交易记录不存在 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **transactionsCreate**
> TransactionDTO transactionsCreate(transactionRequest)


### Example

```typescript
import {
    TransactionsApi,
    Configuration,
    TransactionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionsApi(configuration);

let transactionRequest: TransactionRequest; //

const { status, data } = await apiInstance.transactionsCreate(
    transactionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **transactionRequest** | **TransactionRequest**|  | |


### Return type

**TransactionDTO**

### Authorization

No authorization required

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

# **transactionsCreate_0**
> TransactionDTO transactionsCreate_0(transactionRequest)


### Example

```typescript
import {
    TransactionsApi,
    Configuration,
    TransactionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionsApi(configuration);

let transactionRequest: TransactionRequest; //

const { status, data } = await apiInstance.transactionsCreate_0(
    transactionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **transactionRequest** | **TransactionRequest**|  | |


### Return type

**TransactionDTO**

### Authorization

No authorization required

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

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 交易记录列表 |  -  |
|**403** | 权限不足（普通用户尝试访问未关联的数据） |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **transactionsList_0**
> TransactionListResponse transactionsList_0()


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

const { status, data } = await apiInstance.transactionsList_0(
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

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 交易记录列表 |  -  |
|**403** | 权限不足（普通用户尝试访问未关联的数据） |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateTransaction**
> TransactionDTO updateTransaction(transactionRequest)


### Example

```typescript
import {
    TransactionsApi,
    Configuration,
    TransactionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionsApi(configuration);

let id: string; // (default to undefined)
let transactionRequest: TransactionRequest; //

const { status, data } = await apiInstance.updateTransaction(
    id,
    transactionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **transactionRequest** | **TransactionRequest**|  | |
| **id** | [**string**] |  | defaults to undefined|


### Return type

**TransactionDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 交易记录已更新 |  -  |
|**400** | 无效输入（交易类型不存在或参数格式错误） |  -  |
|**403** | 权限不足（普通用户尝试访问未关联的数据） |  -  |
|**404** | 交易记录不存在 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateTransaction_0**
> TransactionDTO updateTransaction_0(transactionRequest)


### Example

```typescript
import {
    TransactionsApi,
    Configuration,
    TransactionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionsApi(configuration);

let id: string; // (default to undefined)
let transactionRequest: TransactionRequest; //

const { status, data } = await apiInstance.updateTransaction_0(
    id,
    transactionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **transactionRequest** | **TransactionRequest**|  | |
| **id** | [**string**] |  | defaults to undefined|


### Return type

**TransactionDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 交易记录已更新 |  -  |
|**400** | 无效输入（交易类型不存在或参数格式错误） |  -  |
|**403** | 权限不足（普通用户尝试访问未关联的数据） |  -  |
|**404** | 交易记录不存在 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

