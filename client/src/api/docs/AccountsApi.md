# AccountsApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**accountsCreate**](#accountscreate) | **POST** /accounts | 创建顶级账户|
|[**accountsDelete**](#accountsdelete) | **DELETE** /accounts/{accountId} | 删除顶级账户|
|[**accountsList**](#accountslist) | **GET** /accounts | 列出顶级账户|
|[**accountsUpdate**](#accountsupdate) | **PUT** /accounts/{accountId} | 更新顶级账户|

# **accountsCreate**
> Account accountsCreate()

管理员创建新的顶级账户（银行、微信、支付宝等）。

### Example

```typescript
import {
    AccountsApi,
    Configuration,
    AccountRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AccountsApi(configuration);

let accountRequest: AccountRequest; // (optional)

const { status, data } = await apiInstance.accountsCreate(
    accountRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **accountRequest** | **AccountRequest**|  | |


### Return type

**Account**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | 账户已创建 |  -  |
|**400** | 无效输入（名称重复或参数错误） |  -  |
|**403** | 权限不足（非管理员） |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **accountsDelete**
> accountsDelete()

管理员删除指定账户，需确保无关联子账户。

### Example

```typescript
import {
    AccountsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AccountsApi(configuration);

let accountId: number; //账户 ID (default to undefined)

const { status, data } = await apiInstance.accountsDelete(
    accountId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **accountId** | [**number**] | 账户 ID | defaults to undefined|


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
|**204** | 账户已删除 |  -  |
|**403** | 权限不足（非管理员） |  -  |
|**404** | 账户不存在 |  -  |
|**409** | 账户存在关联子账户，无法删除 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **accountsList**
> Array<Account> accountsList()

管理员获取顶级账户列表。

### Example

```typescript
import {
    AccountsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AccountsApi(configuration);

const { status, data } = await apiInstance.accountsList();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<Account>**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 账户列表 |  -  |
|**403** | 权限不足（非管理员） |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **accountsUpdate**
> Account accountsUpdate()

管理员更新指定账户的名称或类型。

### Example

```typescript
import {
    AccountsApi,
    Configuration,
    AccountRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AccountsApi(configuration);

let accountId: number; //账户 ID (default to undefined)
let accountRequest: AccountRequest; // (optional)

const { status, data } = await apiInstance.accountsUpdate(
    accountId,
    accountRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **accountRequest** | **AccountRequest**|  | |
| **accountId** | [**number**] | 账户 ID | defaults to undefined|


### Return type

**Account**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 账户已更新 |  -  |
|**400** | 无效输入（名称重复或参数错误） |  -  |
|**403** | 权限不足（非管理员） |  -  |
|**404** | 账户不存在 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

