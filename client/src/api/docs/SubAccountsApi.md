# SubAccountsApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**subAccountsCreate**](#subaccountscreate) | **POST** /sub-accounts | 创建子账户|
|[**subAccountsDelete**](#subaccountsdelete) | **DELETE** /sub-accounts/{subAccountId} | 删除子账户|
|[**subAccountsUpdate**](#subaccountsupdate) | **PUT** /sub-accounts/{subAccountId} | 更新子账户|

# **subAccountsCreate**
> SubAccount subAccountsCreate()

管理员创建新的子账户。需提供唯一子账户编号和初始余额。

### Example

```typescript
import {
    SubAccountsApi,
    Configuration,
    SubAccountRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SubAccountsApi(configuration);

let subAccountRequest: SubAccountRequest; // (optional)

const { status, data } = await apiInstance.subAccountsCreate(
    subAccountRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **subAccountRequest** | **SubAccountRequest**|  | |


### Return type

**SubAccount**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | 子账户已创建 |  -  |
|**400** | 无效输入（账户编号重复或参数格式错误） |  -  |
|**403** | 权限不足（非管理员） |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **subAccountsDelete**
> subAccountsDelete()

管理员删除指定子账户，需确保无关联交易。

### Example

```typescript
import {
    SubAccountsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SubAccountsApi(configuration);

let subAccountId: number; // (default to undefined)

const { status, data } = await apiInstance.subAccountsDelete(
    subAccountId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **subAccountId** | [**number**] |  | defaults to undefined|


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
|**204** | 子账户已删除 |  -  |
|**403** | 权限不足（非管理员） |  -  |
|**404** | 子账户不存在 |  -  |
|**409** | 子账户存在关联交易，无法删除 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **subAccountsUpdate**
> SubAccount subAccountsUpdate()

管理员更新指定子账户的信息。

### Example

```typescript
import {
    SubAccountsApi,
    Configuration,
    SubAccountRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SubAccountsApi(configuration);

let subAccountId: number; // (default to undefined)
let subAccountRequest: SubAccountRequest; // (optional)

const { status, data } = await apiInstance.subAccountsUpdate(
    subAccountId,
    subAccountRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **subAccountRequest** | **SubAccountRequest**|  | |
| **subAccountId** | [**number**] |  | defaults to undefined|


### Return type

**SubAccount**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 子账户已更新 |  -  |
|**400** | 无效输入（账户编号重复或参数格式错误） |  -  |
|**403** | 权限不足（非管理员） |  -  |
|**404** | 子账户不存在 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

