# TransactionTypesApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**transactionTypesCreate**](#transactiontypescreate) | **POST** /transaction-types | 创建交易类型|
|[**transactionTypesCreate_0**](#transactiontypescreate_0) | **POST** /transaction-types | 创建交易类型|
|[**transactionTypesDelete**](#transactiontypesdelete) | **DELETE** /transaction-types/{typeId} | 删除交易类型|
|[**transactionTypesDelete_0**](#transactiontypesdelete_0) | **DELETE** /transaction-types/{typeId} | 删除交易类型|
|[**transactionTypesList**](#transactiontypeslist) | **GET** /transaction-types | 列出交易类型|
|[**transactionTypesList_0**](#transactiontypeslist_0) | **GET** /transaction-types | 列出交易类型|
|[**transactionTypesUpdate**](#transactiontypesupdate) | **PUT** /transaction-types/{typeId} | 更新交易类型|
|[**transactionTypesUpdate_0**](#transactiontypesupdate_0) | **PUT** /transaction-types/{typeId} | 更新交易类型|

# **transactionTypesCreate**
> TransactionTypeDTO transactionTypesCreate(transactionTypeRequest)


### Example

```typescript
import {
    TransactionTypesApi,
    Configuration,
    TransactionTypeRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionTypesApi(configuration);

let transactionTypeRequest: TransactionTypeRequest; //

const { status, data } = await apiInstance.transactionTypesCreate(
    transactionTypeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **transactionTypeRequest** | **TransactionTypeRequest**|  | |


### Return type

**TransactionTypeDTO**

### Authorization

No authorization required

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

# **transactionTypesCreate_0**
> TransactionTypeDTO transactionTypesCreate_0(transactionTypeRequest)


### Example

```typescript
import {
    TransactionTypesApi,
    Configuration,
    TransactionTypeRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionTypesApi(configuration);

let transactionTypeRequest: TransactionTypeRequest; //

const { status, data } = await apiInstance.transactionTypesCreate_0(
    transactionTypeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **transactionTypeRequest** | **TransactionTypeRequest**|  | |


### Return type

**TransactionTypeDTO**

### Authorization

No authorization required

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

No authorization required

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

# **transactionTypesDelete_0**
> transactionTypesDelete_0()


### Example

```typescript
import {
    TransactionTypesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionTypesApi(configuration);

let typeId: number; //交易类型 ID (default to undefined)

const { status, data } = await apiInstance.transactionTypesDelete_0(
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

No authorization required

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
> TransactionTypeDTO transactionTypesList()


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

**TransactionTypeDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 交易类型列表 |  -  |
|**403** | 权限不足 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **transactionTypesList_0**
> TransactionTypeDTO transactionTypesList_0()


### Example

```typescript
import {
    TransactionTypesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionTypesApi(configuration);

const { status, data } = await apiInstance.transactionTypesList_0();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**TransactionTypeDTO**

### Authorization

No authorization required

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
> TransactionTypeDTO transactionTypesUpdate(transactionTypeRequest)


### Example

```typescript
import {
    TransactionTypesApi,
    Configuration,
    TransactionTypeRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionTypesApi(configuration);

let typeId: number; //交易类型 ID (default to undefined)
let transactionTypeRequest: TransactionTypeRequest; //

const { status, data } = await apiInstance.transactionTypesUpdate(
    typeId,
    transactionTypeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **transactionTypeRequest** | **TransactionTypeRequest**|  | |
| **typeId** | [**number**] | 交易类型 ID | defaults to undefined|


### Return type

**TransactionTypeDTO**

### Authorization

No authorization required

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

# **transactionTypesUpdate_0**
> TransactionTypeDTO transactionTypesUpdate_0(transactionTypeRequest)


### Example

```typescript
import {
    TransactionTypesApi,
    Configuration,
    TransactionTypeRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TransactionTypesApi(configuration);

let typeId: number; //交易类型 ID (default to undefined)
let transactionTypeRequest: TransactionTypeRequest; //

const { status, data } = await apiInstance.transactionTypesUpdate_0(
    typeId,
    transactionTypeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **transactionTypeRequest** | **TransactionTypeRequest**|  | |
| **typeId** | [**number**] | 交易类型 ID | defaults to undefined|


### Return type

**TransactionTypeDTO**

### Authorization

No authorization required

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

