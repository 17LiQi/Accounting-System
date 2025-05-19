// src/mocks/handlers.ts
import { rest } from 'msw';
import type { Account, AccountRequest } from '@/models/accounts';
import { mockAuthApi } from './auth';

const baseURL = '/api';

export const handlers = [
    // 登录接口模拟
    rest.post(`${baseURL}/login`, async (req, res, ctx) => {
        try {
            const body = await req.json();
            const response = await mockAuthApi.login(body);
            return res(ctx.status(response.status), ctx.json(response.data));
        } catch (error: any) {
            return res(
                ctx.status(error.status || 500),
                ctx.json(error.data || { message: '服务器错误' })
            );
        }
    }),

    // 账户列表接口模拟
    rest.get(`${baseURL}/accounts`, (_req, res, ctx) => {
        const accounts: Account[] = [
            {
                accountId: 1,
                accountName: '工商银行',
                accountType: { typeId: 1, typeName: 'BANK' },
                balance: 10000
            },
            {
                accountId: 2,
                accountName: '微信钱包',
                accountType: { typeId: 2, typeName: 'WECHAT' },
                balance: 5000
            }
        ];
        return res(ctx.json(accounts));
    }),
    rest.post(`${baseURL}/accounts`, async (req, res, ctx) => {
        const requestBody = await req.json() as AccountRequest;
        const account: Account = {
            accountId: 3,
            accountName: requestBody.accountName,
            accountType: { typeId: requestBody.typeId, typeName: requestBody.type },
            balance: 0
        };
        return res(ctx.json(account));
    }),
    rest.get(`${baseURL}/accounts/:id`, (req, res, ctx) => {
        const { id } = req.params;
        return res(ctx.json({
            accountId: Number(id),
            accountName: '测试账户',
            accountType: { typeId: 1, typeName: 'BANK' },
            balance: 1000
        }));
    }),
    rest.get(`${baseURL}/accounts/:id/transactions`, (_req, res, ctx) => {
        return res(ctx.json([
            {
                id: '1',
                date: '2024-01-01',
                type: '收入',
                amount: 1000,
                description: '工资'
            }
        ]));
    })
];