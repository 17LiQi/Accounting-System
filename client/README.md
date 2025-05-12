## accounting-system-api@1.0.0

This generator creates TypeScript/JavaScript client that utilizes [axios](https://github.com/axios/axios). The generated Node module can be used in the following environments:

Environment
* Node.js
* Webpack
* Browserify

Language level
* ES5 - you must have a Promises/A+ library installed
* ES6

Module system
* CommonJS
* ES6 module system

It can be used in both TypeScript and JavaScript. In TypeScript, the definition will be automatically resolved via `package.json`. ([Reference](https://www.typescriptlang.org/docs/handbook/declaration-files/consumption.html))

### Building

To build and compile the typescript sources to javascript use:
```
npm install
npm run build
```

### Publishing

First build the package then run `npm publish`

### Consuming

navigate to the folder of your consuming project and run one of the following commands.

_published:_

```
npm install accounting-system-api@1.0.0 --save
```

_unPublished (not recommended):_

```
npm install PATH_TO_GENERATED_PACKAGE --save
```

### Documentation for API Endpoints

All URIs are relative to *http://localhost*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AuthApi* | [**authLogin**](docs/AuthApi.md#authlogin) | **POST** /login | 用户登录
*StatisticsApi* | [**statisticsGet**](docs/StatisticsApi.md#statisticsget) | **GET** /statistics | 获取收支统计
*SubAccountsApi* | [**subAccountsCreate**](docs/SubAccountsApi.md#subaccountscreate) | **POST** /sub-accounts | 创建子账户
*SubAccountsApi* | [**subAccountsDelete**](docs/SubAccountsApi.md#subaccountsdelete) | **DELETE** /sub-accounts/{subAccountId} | 删除子账户
*SubAccountsApi* | [**subAccountsUpdate**](docs/SubAccountsApi.md#subaccountsupdate) | **PUT** /sub-accounts/{subAccountId} | 更新子账户
*TransactionTypesApi* | [**transactionTypesCreate**](docs/TransactionTypesApi.md#transactiontypescreate) | **POST** /transaction-types | 创建交易类型
*TransactionTypesApi* | [**transactionTypesDelete**](docs/TransactionTypesApi.md#transactiontypesdelete) | **DELETE** /transaction-types/{typeId} | 删除交易类型
*TransactionTypesApi* | [**transactionTypesList**](docs/TransactionTypesApi.md#transactiontypeslist) | **GET** /transaction-types | 列出交易类型
*TransactionTypesApi* | [**transactionTypesUpdate**](docs/TransactionTypesApi.md#transactiontypesupdate) | **PUT** /transaction-types/{typeId} | 更新交易类型
*TransactionsApi* | [**transactionsCreate**](docs/TransactionsApi.md#transactionscreate) | **POST** /transactions | 创建交易记录
*TransactionsApi* | [**transactionsList**](docs/TransactionsApi.md#transactionslist) | **GET** /transactions | 列出交易记录
*UsersApi* | [**usersCreate**](docs/UsersApi.md#userscreate) | **POST** /users | 创建用户
*UsersApi* | [**usersDelete**](docs/UsersApi.md#usersdelete) | **DELETE** /users/{userId} | 删除用户
*UsersApi* | [**usersList**](docs/UsersApi.md#userslist) | **GET** /users | 列出用户
*UsersApi* | [**usersUpdate**](docs/UsersApi.md#usersupdate) | **PUT** /users/{userId} | 更新用户


### Documentation For Models

 - [ApiError](docs/ApiError.md)
 - [LoginRequest](docs/LoginRequest.md)
 - [LoginResponse](docs/LoginResponse.md)
 - [StatisticsResponse](docs/StatisticsResponse.md)
 - [StatisticsResponseExpenseByType](docs/StatisticsResponseExpenseByType.md)
 - [StatisticsResponseIncomeByType](docs/StatisticsResponseIncomeByType.md)
 - [SubAccount](docs/SubAccount.md)
 - [SubAccountRequest](docs/SubAccountRequest.md)
 - [Transaction](docs/Transaction.md)
 - [TransactionListResponse](docs/TransactionListResponse.md)
 - [TransactionRequest](docs/TransactionRequest.md)
 - [TransactionType](docs/TransactionType.md)
 - [User](docs/User.md)
 - [UserRequest](docs/UserRequest.md)


<a id="documentation-for-authorization"></a>
## Documentation For Authorization


Authentication schemes defined for the API:
<a id="bearerAuth"></a>
### bearerAuth

- **Type**: Bearer authentication (JWT)

