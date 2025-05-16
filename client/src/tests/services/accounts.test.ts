import { getAccounts } from '@/api/services/accounts';
import { AccountsApi } from '@/api/apis/accounts/accounts-api';
jest.mock('@/api/apis/accounts/accounts-api');
describe('Accounts Service', () => {
    it('fetches accounts', async () => {
        const mockAccounts = [{ id: '1', name: 'Test', balance: 100 }];
        (AccountsApi.prototype.getAccounts as jest.Mock).mockResolvedValue(mockAccounts);
        const result = await getAccounts();
        expect(result).toEqual(mockAccounts);
    });
});