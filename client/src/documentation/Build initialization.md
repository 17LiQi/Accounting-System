# Accounting System API @1.0.0

本生成器创建使用 [axios](https://github.com/axios/axios) 的 TypeScript/JavaScript 客户端。生成的 Node 模块可在以下环境中使用：

### 环境
- Node.js (本项目实际使用环境)
- Webpack
- Browserify

### 语言级别
- ES5 - 需安装 Promises/A+ 库
- ES6 (默认使用新的 ES6 模块语法)

### 模块系统
- CommonJS
- ES6 模块系统

可同时用于 TypeScript 和 JavaScript。在 TypeScript 中，定义会通过 `package.json` 自动解析。([参考](https://www.typescriptlang.org/docs/handbook/declaration-files/consumption.html))


## 构建(建议使用pnpm,减缓磁盘占用)

要构建 TypeScript 源文件并编译为 JavaScript，请执行：
```bash
pnpm install
pnpm run build
```


## 发布

首先构建包，然后运行：
```bash
pnpm publish
```


## 使用

导航到使用项目的文件夹，然后运行以下命令之一：

### 已发布版本：
```bash
pnpm install accounting-system-api@1.0.0 --save
```

### 未发布版本（不推荐）：
```bash
pnpm install PATH_TO_GENERATED_PACKAGE --save
```


## API 端点文档

所有 URI 均相对于 *http://localhost*

| 类          | 方法                | HTTP 请求 | 描述               |
|-------------|---------------------|------------|--------------------|
| *AuthApi*   | [**authLogin**](docs/AuthApi.md#authlogin) | **POST** /login | 用户登录           |
| *StatisticsApi* | [**statisticsGet**](docs/StatisticsApi.md#statisticsget) | **GET** /statistics | 获取收支统计       |
| *SubAccountsApi* | [**subAccountsCreate**](docs/SubAccountsApi.md#subaccountscreate) | **POST** /sub-accounts | 创建子账户         |
| *SubAccountsApi* | [**subAccountsDelete**](docs/SubAccountsApi.md#subaccountsdelete) | **DELETE** /sub-accounts/{subAccountId} | 删除子账户         |
| *SubAccountsApi* | [**subAccountsUpdate**](docs/SubAccountsApi.md#subaccountsupdate) | **PUT** /sub-accounts/{subAccountId} | 更新子账户         |
| *TransactionTypesApi* | [**transactionTypesCreate**](docs/TransactionTypesApi.md#transactiontypescreate) | **POST** /transaction-types | 创建交易类型       |
| *TransactionTypesApi* | [**transactionTypesDelete**](docs/TransactionTypesApi.md#transactiontypesdelete) | **DELETE** /transaction-types/{typeId} | 删除交易类型       |
| *TransactionTypesApi* | [**transactionTypesList**](docs/TransactionTypesApi.md#transactiontypeslist) | **GET** /transaction-types | 列出交易类型       |
| *TransactionTypesApi* | [**transactionTypesUpdate**](docs/TransactionTypesApi.md#transactiontypesupdate) | **PUT** /transaction-types/{typeId} | 更新交易类型       |
| *TransactionsApi* | [**transactionsCreate**](docs/TransactionsApi.md#transactionscreate) | **POST** /transactions | 创建交易记录       |
| *TransactionsApi* | [**transactionsList**](docs/TransactionsApi.md#transactionslist) | **GET** /transactions | 列出交易记录       |
| *UsersApi*    | [**usersCreate**](docs/UsersApi.md#userscreate) | **POST** /users | 创建用户           |
| *UsersApi*    | [**usersDelete**](docs/UsersApi.md#usersdelete) | **DELETE** /users/{userId} | 删除用户           |
| *UsersApi*    | [**usersList**](docs/UsersApi.md#userslist) | **GET** /users | 列出用户           |
| *UsersApi*    | [**usersUpdate**](docs/UsersApi.md#usersupdate) | **PUT** /users/{userId} | 更新用户           |


## 模型文档

- [ApiError](docs/ApiError.md) - API 错误信息
- [LoginRequest](docs/LoginRequest.md) - 登录请求参数
- [LoginResponse](docs/LoginResponse.md) - 登录响应结果
- [StatisticsResponse](docs/StatisticsResponse.md) - 收支统计响应
- [StatisticsResponseExpenseByType](docs/StatisticsResponseExpenseByType.md) - 按类型分类的支出统计
- [StatisticsResponseIncomeByType](docs/StatisticsResponseIncomeByType.md) - 按类型分类的收入统计
- [SubAccount](docs/SubAccount.md) - 子账户信息
- [SubAccountRequest](docs/SubAccountRequest.md) - 创建/更新子账户请求参数
- [Transaction](docs/Transaction.md) - 交易记录信息
- [TransactionListResponse](docs/TransactionListResponse.md) - 交易记录列表响应
- [TransactionRequest](docs/TransactionRequest.md) - 创建/更新交易记录请求参数
- [TransactionType](docs/TransactionType.md) - 交易类型信息
- [User](docs/User.md) - 用户信息
- [UserRequest](docs/UserRequest.md) - 创建/更新用户请求参数


## 授权文档

API 定义的认证方案：
### bearerAuth（Bearer 认证，JWT）
- **类型**：Bearer 令牌认证（JSON Web Token）
