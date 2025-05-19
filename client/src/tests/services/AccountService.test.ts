import { AccountService } from '@/api/services';
import type { AccountRequest } from '@/models/accounts';
import { rest } from 'msw';
import { setupServer } from 'msw/node';

const baseUrl = '/api/accounts';

const server = setupServer(
  rest.get(baseUrl, (_req, res, ctx) => {
    return res(ctx.json([
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
    ]));
  }),

  rest.post(baseUrl, async (req, res, ctx) => {
    const body = await req.json() as AccountRequest;
    return res(ctx.json({
      accountId: 3,
      accountName: body.accountName,
      accountType: { typeId: body.typeId, typeName: body.type },
      balance: 0
    }));
  }),

  rest.get(`${baseUrl}/:id`, (req, res, ctx) => {
    return res(ctx.json({
      accountId: Number(req.params.id),
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
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

describe('AccountService', () => {
  it('获取账户列表', async () => {
    const response = await AccountService.getAccounts();
    expect(response.data).toHaveLength(2);
    expect(response.data[0]).toHaveProperty('accountName', '银行账户');
  });

  it('创建账户', async () => {
    const request: AccountRequest = {
      accountName: '测试账户',
      typeId: 1,
      type: 'BANK'
    };
    const response = await AccountService.createAccount(request);
    expect(response.data).toHaveProperty('accountName', '测试账户');
  });

  it('获取账户详情', async () => {
    const response = await AccountService.getAccount(1);
    expect(response.data).toHaveProperty('accountId', 1);
  });

  it('获取账户交易记录', async () => {
    const response = await AccountService.getAccountTransactions(1);
    expect(response.data).toHaveLength(1);
    expect(response.data[0]).toHaveProperty('type', '收入');
  });
}); 