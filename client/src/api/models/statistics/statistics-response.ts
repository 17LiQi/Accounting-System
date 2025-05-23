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


// May contain unused imports in some cases
// @ts-ignore
import type { StatisticsResponseExpenseByType } from './statistics-response-expense-by-type';
// May contain unused imports in some cases
// @ts-ignore
import type { StatisticsResponseIncomeByType } from './statistics-response-income-by-type';

/**
 * 
 * @export
 * @interface StatisticsResponse
 */
export interface StatisticsResponse {
    /**
     * 统计周期：daily, weekly, monthly, yearly
     * @type {string}
     * @memberof StatisticsResponse
     */
    'period': StatisticsResponsePeriodEnum;
    /**
     * 年份
     * @type {number}
     * @memberof StatisticsResponse
     */
    'year': number;
    /**
     * 月份（月度统计时需要）
     * @type {number}
     * @memberof StatisticsResponse
     */
    'month'?: number;
    /**
     * 周次（周度统计时需要）
     * @type {number}
     * @memberof StatisticsResponse
     */
    'week'?: number;
    /**
     * 日期（日度统计时需要）
     * @type {number}
     * @memberof StatisticsResponse
     */
    'day'?: number;
    /**
     * 按类型统计的收入
     * @type {Array<StatisticsResponseIncomeByType>}
     * @memberof StatisticsResponse
     */
    'incomeByType'?: Array<StatisticsResponseIncomeByType>;
    /**
     * 按类型统计的支出
     * @type {Array<StatisticsResponseExpenseByType>}
     * @memberof StatisticsResponse
     */
    'expenseByType'?: Array<StatisticsResponseExpenseByType>;
    /**
     * 总收入（固定 2 位小数）
     * @type {string}
     * @memberof StatisticsResponse
     */
    'totalIncome': string;
    /**
     * 总支出（固定 2 位小数）
     * @type {string}
     * @memberof StatisticsResponse
     */
    'totalExpense': string;
}

export const StatisticsResponsePeriodEnum = {
    Daily: 'daily',
    Weekly: 'weekly',
    Monthly: 'monthly',
    Yearly: 'yearly'
} as const;

export type StatisticsResponsePeriodEnum = typeof StatisticsResponsePeriodEnum[keyof typeof StatisticsResponsePeriodEnum];


