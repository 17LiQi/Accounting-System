# SubAccountsApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getSubAccount**](#getsubaccount) | **GET** /sub-accounts/{id} | 获取子账户详情|
|[**getSubAccount_0**](#getsubaccount_0) | **GET** /sub-accounts/{id} | 获取子账户详情|
|[**getSubAccounts**](#getsubaccounts) | **GET** /sub-accounts | 列出子账户|
|[**getSubAccounts_0**](#getsubaccounts_0) | **GET** /sub-accounts | 列出子账户|
|[**subAccountsCreate**](#subaccountscreate) | **POST** /sub-accounts | 创建子账户|
|[**subAccountsCreate_0**](#subaccountscreate_0) | **POST** /sub-accounts | 创建子账户|
|[**subAccountsDelete**](#subaccountsdelete) | **DELETE** /sub-accounts/{id} | 删除子账户|
|[**subAccountsDelete_0**](#subaccountsdelete_0) | **DELETE** /sub-accounts/{id} | 删除子账户|
|[**subAccountsUpdate**](#subaccountsupdate) | **PUT** /sub-accounts/{id} | 更新子账户|
|[**subAccountsUpdate_0**](#subaccountsupdate_0) | **PUT** /sub-accounts/{id} | 更新子账户|

# **getSubAccount**
> SubAccountDTO getSubAccount()


### Example

```typescript
import {
    SubAccountsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SubAccountsApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.getSubAccount(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**SubAccountDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 子账户详情 |  -  |
|**403** | 权限不足 |  -  |
|**404** | 子账户不存在 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getSubAccount_0**
> SubAccountDTO getSubAccount_0()


### Example

```typescript
import {
    SubAccountsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SubAccountsApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.getSubAccount_0(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**SubAccountDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 子账户详情 |  -  |
|**403** | 权限不足 |  -  |
|**404** | 子账户不存在 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getSubAccounts**
> SubAccountDTO getSubAccounts()


### Example

```typescript
import {
    SubAccountsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SubAccountsApi(configuration);

const { status, data } = await apiInstance.getSubAccounts();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**SubAccountDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 子账户列表 |  -  |
|**403** | 权限不足 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getSubAccounts_0**
> SubAccountDTO getSubAccounts_0()


### Example

```typescript
import {
    SubAccountsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SubAccountsApi(configuration);

const { status, data } = await apiInstance.getSubAccounts_0();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**SubAccountDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 子账户列表 |  -  |
|**403** | 权限不足 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **subAccountsCreate**
> SubAccount subAccountsCreate(subAccountRequest)


### Example

```typescript
import {
    SubAccountsApi,
    Configuration,
    SubAccountRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SubAccountsApi(configuration);

let subAccountRequest: SubAccountRequest; //

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

No authorization required

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

# **subAccountsCreate_0**
> SubAccount subAccountsCreate_0(subAccountRequest)


### Example

```typescript
import {
    SubAccountsApi,
    Configuration,
    SubAccountRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SubAccountsApi(configuration);

let subAccountRequest: SubAccountRequest; //

const { status, data } = await apiInstance.subAccountsCreate_0(
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

No authorization required

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


### Example

```typescript
import {
    SubAccountsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SubAccountsApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.subAccountsDelete(
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
|**204** | 子账户已删除 |  -  |
|**403** | 权限不足（非管理员） |  -  |
|**404** | 子账户不存在 |  -  |
|**409** | 子账户存在关联交易，无法删除 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **subAccountsDelete_0**
> subAccountsDelete_0()


### Example

```typescript
import {
    SubAccountsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SubAccountsApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.subAccountsDelete_0(
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
|**204** | 子账户已删除 |  -  |
|**403** | 权限不足（非管理员） |  -  |
|**404** | 子账户不存在 |  -  |
|**409** | 子账户存在关联交易，无法删除 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **subAccountsUpdate**
> SubAccount subAccountsUpdate(subAccountRequest)


### Example

```typescript
import {
    SubAccountsApi,
    Configuration,
    SubAccountRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SubAccountsApi(configuration);

let id: string; // (default to undefined)
let subAccountRequest: SubAccountRequest; //

const { status, data } = await apiInstance.subAccountsUpdate(
    id,
    subAccountRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **subAccountRequest** | **SubAccountRequest**|  | |
| **id** | [**string**] |  | defaults to undefined|


### Return type

**SubAccount**

### Authorization

No authorization required

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

# **subAccountsUpdate_0**
> SubAccount subAccountsUpdate_0(subAccountRequest)


### Example

```typescript
import {
    SubAccountsApi,
    Configuration,
    SubAccountRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SubAccountsApi(configuration);

let id: string; // (default to undefined)
let subAccountRequest: SubAccountRequest; //

const { status, data } = await apiInstance.subAccountsUpdate_0(
    id,
    subAccountRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **subAccountRequest** | **SubAccountRequest**|  | |
| **id** | [**string**] |  | defaults to undefined|


### Return type

**SubAccount**

### Authorization

No authorization required

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

