# StatisticsApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**statisticsGet**](#statisticsget) | **GET** /statistics | |
|[**statisticsGet1**](#statisticsget1) | **GET** /statistics/statistics | 获取收支统计|
|[**statisticsGet1_0**](#statisticsget1_0) | **GET** /statistics/statistics | 获取收支统计|

# **statisticsGet**
> StatisticsResponse statisticsGet()


### Example

```typescript
import {
    StatisticsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new StatisticsApi(configuration);

let period: string; // (default to undefined)
let year: number; // (default to undefined)
let userId: number; // (optional) (default to undefined)
let accountId: number; // (optional) (default to undefined)
let subAccountId: number; // (optional) (default to undefined)
let month: number; // (optional) (default to undefined)
let week: number; // (optional) (default to undefined)
let day: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.statisticsGet(
    period,
    year,
    userId,
    accountId,
    subAccountId,
    month,
    week,
    day
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **period** | [**string**] |  | defaults to undefined|
| **year** | [**number**] |  | defaults to undefined|
| **userId** | [**number**] |  | (optional) defaults to undefined|
| **accountId** | [**number**] |  | (optional) defaults to undefined|
| **subAccountId** | [**number**] |  | (optional) defaults to undefined|
| **month** | [**number**] |  | (optional) defaults to undefined|
| **week** | [**number**] |  | (optional) defaults to undefined|
| **day** | [**number**] |  | (optional) defaults to undefined|


### Return type

**StatisticsResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **statisticsGet1**
> StatisticsResponse statisticsGet1()


### Example

```typescript
import {
    StatisticsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new StatisticsApi(configuration);

let period: 'monthly' | 'annual'; //统计周期（月度或年度） (default to undefined)
let year: number; //年份 (default to undefined)
let userId: number; //按用户过滤（管理员专用，普通用户自动使用 JWT userId） (optional) (default to undefined)
let accountId: number; //按顶级账号过滤 (optional) (default to undefined)
let subAccountId: number; //按子账户过滤 (optional) (default to undefined)
let month: number; //月份（月度统计时需要） (optional) (default to undefined)

const { status, data } = await apiInstance.statisticsGet1(
    period,
    year,
    userId,
    accountId,
    subAccountId,
    month
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **period** | [**&#39;monthly&#39; | &#39;annual&#39;**]**Array<&#39;monthly&#39; &#124; &#39;annual&#39;>** | 统计周期（月度或年度） | defaults to undefined|
| **year** | [**number**] | 年份 | defaults to undefined|
| **userId** | [**number**] | 按用户过滤（管理员专用，普通用户自动使用 JWT userId） | (optional) defaults to undefined|
| **accountId** | [**number**] | 按顶级账号过滤 | (optional) defaults to undefined|
| **subAccountId** | [**number**] | 按子账户过滤 | (optional) defaults to undefined|
| **month** | [**number**] | 月份（月度统计时需要） | (optional) defaults to undefined|


### Return type

**StatisticsResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 统计数据 |  -  |
|**400** | 无效参数（参数格式错误） |  -  |
|**403** | 权限不足（普通用户尝试访问未关联的数据） |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **statisticsGet1_0**
> StatisticsResponse statisticsGet1_0()


### Example

```typescript
import {
    StatisticsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new StatisticsApi(configuration);

let period: 'monthly' | 'annual'; //统计周期（月度或年度） (default to undefined)
let year: number; //年份 (default to undefined)
let userId: number; //按用户过滤（管理员专用，普通用户自动使用 JWT userId） (optional) (default to undefined)
let accountId: number; //按顶级账号过滤 (optional) (default to undefined)
let subAccountId: number; //按子账户过滤 (optional) (default to undefined)
let month: number; //月份（月度统计时需要） (optional) (default to undefined)

const { status, data } = await apiInstance.statisticsGet1_0(
    period,
    year,
    userId,
    accountId,
    subAccountId,
    month
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **period** | [**&#39;monthly&#39; | &#39;annual&#39;**]**Array<&#39;monthly&#39; &#124; &#39;annual&#39;>** | 统计周期（月度或年度） | defaults to undefined|
| **year** | [**number**] | 年份 | defaults to undefined|
| **userId** | [**number**] | 按用户过滤（管理员专用，普通用户自动使用 JWT userId） | (optional) defaults to undefined|
| **accountId** | [**number**] | 按顶级账号过滤 | (optional) defaults to undefined|
| **subAccountId** | [**number**] | 按子账户过滤 | (optional) defaults to undefined|
| **month** | [**number**] | 月份（月度统计时需要） | (optional) defaults to undefined|


### Return type

**StatisticsResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 统计数据 |  -  |
|**400** | 无效参数（参数格式错误） |  -  |
|**403** | 权限不足（普通用户尝试访问未关联的数据） |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

