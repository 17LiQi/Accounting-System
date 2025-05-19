// src/mocks/handlers.ts
import { rest } from 'msw';
import type { Account, AccountRequest } from '@/models/accounts';

const baseUrl = '/api/accounts';

export const handlers = [
    rest.get(baseUrl, (_req, res, ctx) => {
        const accounts: Account[] = [
            {
                accountId: 1,
                accountName: '银行账户',
                accountType: { typeId: 1, typeName: 'BANK' },
                balance: 1000
            },
            {
                accountId: 2,
                accountName: '微信账户',
                accountType: { typeId: 2, typeName: 'WECHAT' },
                balance: 500
            }
        ];
        return res(ctx.json(accounts));
    }),
    rest.post(baseUrl, async (req, res, ctx) => {
        const requestBody = await req.json() as AccountRequest;
        const account: Account = {
            accountId: 3,
            accountName: requestBody.accountName,
            accountType: { typeId: requestBody.typeId, typeName: requestBody.type },
            balance: 0
        };
        return res(ctx.json(account));
    }),
    rest.get(`${baseUrl}/:id`, (req, res, ctx) => {
        const { id } = req.params;
        return res(ctx.json({
            accountId: Number(id),
            accountName: '测试账户',
            accountType: { typeId: 1, typeName: 'BANK' },
            balance: 1000
        }));
    }),
    rest.get(`${baseUrl}/:id/transactions`, (_req, res, ctx) => {
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