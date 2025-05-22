// src/store/modules/accounts.ts
import { defineStore } from 'pinia';
import type { AccountDTO } from '@/api/models/accounts';
import type { AccountRequest } from '@/api/models/accounts/account-request';
import { accountsService } from '@/api/services/accounts';

export const useAccountsStore = defineStore('accounts', {
    state: () => ({
        accounts: [] as AccountDTO[],
        loading: false,
        error: null as string | null
    }),

    getters: {
        getAccountById: (state) => (id: number) => {
            return state.accounts.find(account => account.accountId === id);
        },
    },

    actions: {
        async fetchAccounts() {
            this.loading = true;
            this.error = null;
            try {
                this.accounts = await accountsService.getAccounts();
                return this.accounts;
            } catch (error: any) {
                this.error = error.message || '获取账户列表失败';
                throw error;
            } finally {
                this.loading = false;
            }
        },

        async createAccount(request: AccountRequest) {
            this.loading = true;
            this.error = null;
            try {
                const account = await accountsService.createAccount(request);
                this.accounts.push(account);
                return account;
            } catch (error: any) {
                this.error = error.message || '创建账户失败';
                throw error;
            } finally {
                this.loading = false;
            }
        },

        async updateAccount(id: number, request: AccountRequest) {
            this.loading = true;
            this.error = null;
            try {
                const account = await accountsService.updateAccount(id, request);
                const index = this.accounts.findIndex(a => a.accountId === id);
                if (index !== -1) {
                    this.accounts[index] = account;
                }
                return account;
            } catch (error: any) {
                this.error = error.message || '更新账户失败';
                throw error;
            } finally {
                this.loading = false;
            }
        },

        async deleteAccount(id: number) {
            this.loading = true;
            this.error = null;
            try {
                await accountsService.deleteAccount(id);
                this.accounts = this.accounts.filter(a => a.accountId !== id);
            } catch (error: any) {
                this.error = error.message || '删除账户失败';
                throw error;
            } finally {
                this.loading = false;
            }
        },

        clearError() {
            this.error = null;
        },
    },
});