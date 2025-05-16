import { AccountsApi } from '../apis/accounts/accounts-api';
import { Configuration } from '../configuration';
import { Account } from '../models/accounts/account';
const config = new Configuration({ basePath: import.meta.env.VITE_API_BASE_URL });
const accountsApi = new AccountsApi(config);
export const getAccounts = async (): Promise<Account[]> => {
    return await accountsApi.getAccounts();
};
export const getAccount = async (id: string): Promise<Account> => {
    return await accountsApi.getAccountById(id);
};