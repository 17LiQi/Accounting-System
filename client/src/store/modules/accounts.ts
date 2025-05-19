// src/store/modules/accounts.ts
import { defineStore } from 'pinia';
import { AccountService } from '@/api/services';
import type { Account, AccountRequest } from '@/models/accounts';

export const useAccountStore = defineStore('accounts', {
    state: () => ({
        accounts: [] as Account[],
        currentAccount: null as Account | null,
        loading: false,
        error: null as string | null,
    }),
    actions: {
        async fetchAccounts() {
            this.loading = true;
            try {
                const response = await AccountService.getAccounts();
                this.accounts = response.data;
            } catch (error) {
                this.error = '获取账户列表失败';
                console.error(error);
            } finally {
                this.loading = false;
            }
        },
        async createAccount(request: AccountRequest) {
            this.loading = true;
            try {
                const response = await AccountService.createAccount(request);
                this.accounts.push(response.data);
                return response.data;
            } catch (error) {
                this.error = '创建账户失败';
                console.error(error);
                throw error;
            } finally {
                this.loading = false;
            }
        },
    },
});