# UsersApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**usersCreate**](#userscreate) | **POST** /users | 创建用户|
|[**usersDelete**](#usersdelete) | **DELETE** /users/{userId} | 删除用户|
|[**usersList**](#userslist) | **GET** /users | 列出用户|
|[**usersUpdate**](#usersupdate) | **PUT** /users/{userId} | 更新用户|

# **usersCreate**
> User usersCreate()

管理员创建新用户，指定用户名、密码和角色（ADMIN 或 USER）。

### Example

```typescript
import {
    UsersApi,
    Configuration,
    UserRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let userRequest: UserRequest; // (optional)

const { status, data } = await apiInstance.usersCreate(
    userRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **userRequest** | **UserRequest**|  | |


### Return type

**User**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | 用户已创建 |  -  |
|**400** | 无效输入（用户名重复或参数错误） |  -  |
|**403** | 权限不足（非管理员） |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **usersDelete**
> usersDelete()

管理员删除指定用户，需确保用户无关联数据（如交易记录）。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let userId: number; //用户 ID (default to undefined)

const { status, data } = await apiInstance.usersDelete(
    userId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **userId** | [**number**] | 用户 ID | defaults to undefined|


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
|**204** | 用户已删除 |  -  |
|**403** | 权限不足（非管理员） |  -  |
|**404** | 用户不存在 |  -  |
|**409** | 用户存在关联数据（如交易记录），无法删除 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **usersList**
> Array<User> usersList()

管理员获取用户列表。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

const { status, data } = await apiInstance.usersList();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<User>**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 用户列表 |  -  |
|**403** | 权限不足（非管理员） |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **usersUpdate**
> User usersUpdate()

管理员更新指定用户的信息（用户名、密码或角色）。

### Example

```typescript
import {
    UsersApi,
    Configuration,
    UserRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let userId: number; //用户 ID (default to undefined)
let userRequest: UserRequest; // (optional)

const { status, data } = await apiInstance.usersUpdate(
    userId,
    userRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **userRequest** | **UserRequest**|  | |
| **userId** | [**number**] | 用户 ID | defaults to undefined|


### Return type

**User**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 用户已更新 |  -  |
|**400** | 无效输入（用户名重复或参数错误） |  -  |
|**403** | 权限不足（非管理员） |  -  |
|**404** | 用户不存在 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

