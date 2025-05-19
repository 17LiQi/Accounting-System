# AccountsApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**accountsCreate**](#accountscreate) | **POST** /accounts | 创建顶级账户|
|[**accountsCreate_0**](#accountscreate_0) | **POST** /accounts | 创建顶级账户|
|[**accountsDelete**](#accountsdelete) | **DELETE** /accounts/{id} | 删除顶级账户|
|[**accountsDelete_0**](#accountsdelete_0) | **DELETE** /accounts/{id} | 删除顶级账户|
|[**accountsList**](#accountslist) | **GET** /accounts | 列出顶级账户|
|[**accountsList_0**](#accountslist_0) | **GET** /accounts | 列出顶级账户|
|[**accountsUpdate**](#accountsupdate) | **PUT** /accounts/{id} | 更新顶级账户|
|[**accountsUpdate_0**](#accountsupdate_0) | **PUT** /accounts/{id} | 更新顶级账户|

# **accountsCreate**
> Account accountsCreate(accountRequest)


### Example

```typescript
import {
    AccountsApi,
    Configuration,
    AccountRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AccountsApi(configuration);

let accountRequest: AccountRequest; //

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

No authorization required

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

# **accountsCreate_0**
> Account accountsCreate_0(accountRequest)


### Example

```typescript
import {
    AccountsApi,
    Configuration,
    AccountRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AccountsApi(configuration);

let accountRequest: AccountRequest; //

const { status, data } = await apiInstance.accountsCreate_0(
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

No authorization required

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


### Example

```typescript
import {
    AccountsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AccountsApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.accountsDelete(
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
|**204** | 账户已删除 |  -  |
|**403** | 权限不足（非管理员） |  -  |
|**404** | 账户不存在 |  -  |
|**409** | 账户存在关联子账户，无法删除 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **accountsDelete_0**
> accountsDelete_0()


### Example

```typescript
import {
    AccountsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AccountsApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.accountsDelete_0(
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
|**204** | 账户已删除 |  -  |
|**403** | 权限不足（非管理员） |  -  |
|**404** | 账户不存在 |  -  |
|**409** | 账户存在关联子账户，无法删除 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **accountsList**
> Account accountsList()


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

**Account**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 账户列表 |  -  |
|**403** | 权限不足（非管理员） |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **accountsList_0**
> Account accountsList_0()


### Example

```typescript
import {
    AccountsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AccountsApi(configuration);

const { status, data } = await apiInstance.accountsList_0();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Account**

### Authorization

No authorization required

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
> Account accountsUpdate(accountRequest)


### Example

```typescript
import {
    AccountsApi,
    Configuration,
    AccountRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AccountsApi(configuration);

let id: string; // (default to undefined)
let accountRequest: AccountRequest; //

const { status, data } = await apiInstance.accountsUpdate(
    id,
    accountRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **accountRequest** | **AccountRequest**|  | |
| **id** | [**string**] |  | defaults to undefined|


### Return type

**Account**

### Authorization

No authorization required

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

# **accountsUpdate_0**
> Account accountsUpdate_0(accountRequest)


### Example

```typescript
import {
    AccountsApi,
    Configuration,
    AccountRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AccountsApi(configuration);

let id: string; // (default to undefined)
let accountRequest: AccountRequest; //

const { status, data } = await apiInstance.accountsUpdate_0(
    id,
    accountRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **accountRequest** | **AccountRequest**|  | |
| **id** | [**string**] |  | defaults to undefined|


### Return type

**Account**

### Authorization

No authorization required

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

