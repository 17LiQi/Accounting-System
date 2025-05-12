# StatisticsApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**statisticsGet**](#statisticsget) | **GET** /statistics | 获取收支统计|

# **statisticsGet**
> StatisticsResponse statisticsGet()

获取指定周期的收支统计，按交易类型分组。管理员可按 userId 过滤；普通用户仅可查看自己的统计。 

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

const { status, data } = await apiInstance.statisticsGet(
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

[bearerAuth](../README.md#bearerAuth)

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

