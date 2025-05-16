import { rest } from 'msw';
import { Account } from '@/models/accounts/account';
export const handlers = [
    rest.get(`${import.meta.env.VITE_API_BASE_URL}/accounts`, (req, res, ctx) => {
        const accounts: Account[] = [
            { id: '1', name: 'Account 1', balance: 100 },
            { id: '2', name: 'Account 2', balance: 200 },
        ];
        return res(ctx.json(accounts));
    }),
    rest.get(`${import.meta.env.VITE_API_BASE_URL}/accounts/:id`, (req, res, ctx) => {
        const account: Account = { id: req.params.id as string, name: 'Mock Account', balance: 100 };
        return res(ctx.json(account));
    }),
];

rest.get(`${import.meta.env.VITE_API_BASE_URL}/accounts`, (req, res, ctx) => {
    const accounts: Account[] = [
        { id: '1', name: 'Account 1', balance: 100 },
        { id: '2', name: 'Account 2', balance: 200 },
    ];
    return res(ctx.json(accounts));
});