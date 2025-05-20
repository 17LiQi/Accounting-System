// src/mocks/handlers.ts
import { rest } from 'msw';
import type { AccountDTO } from '@/api/models/accounts';
import { mockAccounts, mockAccountTypes } from '@/mocks/accounts';
import { mockAuthApi } from './auth';

const baseURL = 'http://localhost:3000/api';

export const handlers = [
    // 登录接口模拟
    rest.post(`${baseURL}/auth/login`, async (req, res, ctx) => {
        try {
            const body = await req.json();
            const response = await mockAuthApi.authLogin(body);
            return res(ctx.status(response.status), ctx.json(response.data));
        } catch (error: any) {
            return res(
                ctx.status(error.status || 500),
                ctx.json(error.data || { message: '服务器错误' })
            );
        }
    }),

    // 获取账户列表
    rest.get(`${baseURL}/accounts`, (_req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json(mockAccounts)
        );
    }),

    // 获取账户类型列表
    rest.get(`${baseURL}/account-types`, (_req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json(mockAccountTypes)
        );
    }),

    // 创建账户
    rest.post(`${baseURL}/accounts`, async (req, res, ctx) => {
        const newAccount = await req.json();
        const account: AccountDTO = {
            accountId: mockAccounts.length + 1,
            accountName: newAccount.accountName,
            accountType: newAccount.type,
            description: newAccount.description || '',
            balance: 0,
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString()
        };
        mockAccounts.push(account);
        return res(
            ctx.status(201),
            ctx.json(account)
        );
    }),

    // 更新账户
    rest.put(`${baseURL}/accounts/:id`, async (req, res, ctx) => {
        const { id } = req.params;
        const updates = await req.json();
        const index = mockAccounts.findIndex((a: AccountDTO) => a.accountId === Number(id));
        
        if (index === -1) {
            return res(
                ctx.status(404),
                ctx.json({ message: '账户不存在' })
            );
        }

        const updatedAccount = {
            ...mockAccounts[index],
            ...updates,
            updatedAt: new Date().toISOString()
        };
        mockAccounts[index] = updatedAccount;

        return res(
            ctx.status(200),
            ctx.json(updatedAccount)
        );
    }),

    // 删除账户
    rest.delete(`${baseURL}/accounts/:id`, (req, res, ctx) => {
        const { id } = req.params;
        const index = mockAccounts.findIndex((a: AccountDTO) => a.accountId === Number(id));
        
        if (index === -1) {
            return res(
                ctx.status(404),
                ctx.json({ message: '账户不存在' })
            );
        }

        mockAccounts.splice(index, 1);
        return res(
            ctx.status(204)
        );
    }),

    // 获取单个账户
    rest.get(`${baseURL}/accounts/:id`, (req, res, ctx) => {
        const { id } = req.params;
        const account = mockAccounts.find((a: AccountDTO) => a.accountId === Number(id));
        
        if (!account) {
            return res(
                ctx.status(404),
                ctx.json({ message: '账户不存在' })
            );
        }

        return res(
            ctx.status(200),
            ctx.json(account)
        );
    })
];