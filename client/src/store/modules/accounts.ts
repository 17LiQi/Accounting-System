import { defineStore } from 'pinia';
import { getAccounts } from '@/api/services/accounts';
import { Account } from '@/models/accounts/account';
export const useAccountStore = defineStore('accounts', {
    state: () => ({
        accounts: [] as Account[],
        loading: false,
    }),
    actions: {
        async fetchAccounts() {
            this.loading = true;
            try {
                this.accounts = await getAccounts();
            } finally {
                this.loading = false;
            }
        },
    },
});