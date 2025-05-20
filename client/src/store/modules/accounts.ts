// src/store/modules/accounts.ts
import { defineStore } from 'pinia';
import type { AccountDTO } from '@/api/models/accounts';
import type { AccountRequest } from '@/api/models/accounts/account-request';
import { accountsService } from '@/api/services/accounts';

interface AccountsState {
    accounts: AccountDTO[];
    loading: boolean;
    error: string | null;
}

export const useAccountStore = defineStore('accounts', {
    state: (): AccountsState => ({
        accounts: [],
        loading: false,
        error: null
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
            } catch (err: any) {
                this.error = err.message;
                throw err;
            } finally {
                this.loading = false;
            }
        },

        async createAccount(request: AccountRequest) {
            this.loading = true;
            this.error = null;
            try {
                const newAccount = await accountsService.createAccount(request);
                this.accounts.push(newAccount);
                return newAccount;
            } catch (err: any) {
                this.error = err.message;
                throw err;
            } finally {
                this.loading = false;
            }
        },

        async updateAccount(accountId: number, request: AccountRequest) {
            this.loading = true;
            this.error = null;
            try {
                const updatedAccount = await accountsService.updateAccount(accountId, request);
                const index = this.accounts.findIndex(a => a.accountId === accountId);
                if (index !== -1) {
                    this.accounts[index] = updatedAccount;
                }
                return updatedAccount;
            } catch (err: any) {
                this.error = err.message;
                throw err;
            } finally {
                this.loading = false;
            }
        },

        async deleteAccount(accountId: number) {
            this.loading = true;
            this.error = null;
            try {
                await accountsService.deleteAccount(accountId);
                this.accounts = this.accounts.filter(a => a.accountId !== accountId);
            } catch (err: any) {
                this.error = err.message;
                throw err;
            } finally {
                this.loading = false;
            }
        },

        clearError() {
            this.error = null;
        },
    },
});