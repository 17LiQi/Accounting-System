/* tslint:disable */
/* eslint-disable */
/**
 * OpenAPI definition
 * 家庭记账系统的 RESTful API，支持用户认证、人员管理、账户管理、交易记录和统计。 支持银行、微信、支付宝等多种账号，每种账号下可有多张卡或子账户。 角色权限： - 管理员：访问所有数据，管理用户、账号、子账户。 - 普通用户：仅访问通过 user_sub_accounts 关联的子账户和自己的交易/统计。 
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


import type { Configuration } from '../configuration';
import type { AxiosPromise, AxiosInstance, RawAxiosRequestConfig } from 'axios';
import globalAxios from 'axios';
// Some imports not used depending on template conditions
// @ts-ignore
import { DUMMY_BASE_URL, assertParamExists, setApiKeyToObject, setBasicAuthToObject, setBearerAuthToObject, setOAuthToObject, setSearchParams, serializeDataIfNeeded, toPathString, createRequestFunction } from '../common';
// @ts-ignore
import { BASE_PATH, COLLECTION_FORMATS, type RequestArgs, BaseAPI, RequiredError, operationServerMap } from '../base';
// @ts-ignore
import type { ApiError } from '../models';
// @ts-ignore
import type { UserDTO } from '../models';
// @ts-ignore
import type { UserRequest } from '../models';
/**
 * UsersApi - axios parameter creator
 * @export
 */
export const UsersApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * 
         * @summary 创建用户
         * @param {UserRequest} userRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        usersCreate: async (userRequest: UserRequest, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'userRequest' is not null or undefined
            assertParamExists('usersCreate', 'userRequest', userRequest)
            const localVarPath = `/users`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'POST', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            localVarHeaderParameter['Content-Type'] = 'application/json';

            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            localVarRequestOptions.data = serializeDataIfNeeded(userRequest, localVarRequestOptions, configuration)

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @summary 删除用户
         * @param {number} userId 用户 ID
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        usersDelete: async (userId: number, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'userId' is not null or undefined
            assertParamExists('usersDelete', 'userId', userId)
            const localVarPath = `/users/{userId}`
                .replace(`{${"userId"}}`, encodeURIComponent(String(userId)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'DELETE', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @summary 列出用户
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        usersList: async (options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/users`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @summary 更新用户
         * @param {number} userId 用户 ID
         * @param {UserRequest} userRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        usersUpdate: async (userId: number, userRequest: UserRequest, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'userId' is not null or undefined
            assertParamExists('usersUpdate', 'userId', userId)
            // verify required parameter 'userRequest' is not null or undefined
            assertParamExists('usersUpdate', 'userRequest', userRequest)
            const localVarPath = `/users/{userId}`
                .replace(`{${"userId"}}`, encodeURIComponent(String(userId)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'PUT', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            localVarHeaderParameter['Content-Type'] = 'application/json';

            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            localVarRequestOptions.data = serializeDataIfNeeded(userRequest, localVarRequestOptions, configuration)

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
    }
};

/**
 * UsersApi - functional programming interface
 * @export
 */
export const UsersApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = UsersApiAxiosParamCreator(configuration)
    return {
        /**
         * 
         * @summary 创建用户
         * @param {UserRequest} userRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async usersCreate(userRequest: UserRequest, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<UserDTO>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.usersCreate(userRequest, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['UsersApi.usersCreate']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * 
         * @summary 删除用户
         * @param {number} userId 用户 ID
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async usersDelete(userId: number, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.usersDelete(userId, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['UsersApi.usersDelete']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * 
         * @summary 列出用户
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async usersList(options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<UserDTO>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.usersList(options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['UsersApi.usersList']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * 
         * @summary 更新用户
         * @param {number} userId 用户 ID
         * @param {UserRequest} userRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async usersUpdate(userId: number, userRequest: UserRequest, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<UserDTO>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.usersUpdate(userId, userRequest, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['UsersApi.usersUpdate']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
    }
};

/**
 * UsersApi - factory interface
 * @export
 */
export const UsersApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = UsersApiFp(configuration)
    return {
        /**
         * 
         * @summary 创建用户
         * @param {UserRequest} userRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        usersCreate(userRequest: UserRequest, options?: RawAxiosRequestConfig): AxiosPromise<UserDTO> {
            return localVarFp.usersCreate(userRequest, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @summary 删除用户
         * @param {number} userId 用户 ID
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        usersDelete(userId: number, options?: RawAxiosRequestConfig): AxiosPromise<void> {
            return localVarFp.usersDelete(userId, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @summary 列出用户
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        usersList(options?: RawAxiosRequestConfig): AxiosPromise<UserDTO> {
            return localVarFp.usersList(options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @summary 更新用户
         * @param {number} userId 用户 ID
         * @param {UserRequest} userRequest 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        usersUpdate(userId: number, userRequest: UserRequest, options?: RawAxiosRequestConfig): AxiosPromise<UserDTO> {
            return localVarFp.usersUpdate(userId, userRequest, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * UsersApi - object-oriented interface
 * @export
 * @class UsersApi
 * @extends {BaseAPI}
 */
export class UsersApi extends BaseAPI {
    /**
     * 
     * @summary 创建用户
     * @param {UserRequest} userRequest 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof UsersApi
     */
    public usersCreate(userRequest: UserRequest, options?: RawAxiosRequestConfig) {
        return UsersApiFp(this.configuration).usersCreate(userRequest, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @summary 删除用户
     * @param {number} userId 用户 ID
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof UsersApi
     */
    public usersDelete(userId: number, options?: RawAxiosRequestConfig) {
        return UsersApiFp(this.configuration).usersDelete(userId, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @summary 列出用户
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof UsersApi
     */
    public usersList(options?: RawAxiosRequestConfig) {
        return UsersApiFp(this.configuration).usersList(options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @summary 更新用户
     * @param {number} userId 用户 ID
     * @param {UserRequest} userRequest 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof UsersApi
     */
    public usersUpdate(userId: number, userRequest: UserRequest, options?: RawAxiosRequestConfig) {
        return UsersApiFp(this.configuration).usersUpdate(userId, userRequest, options).then((request) => request(this.axios, this.basePath));
    }
}

