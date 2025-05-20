import { defineStore } from 'pinia';
import type { AccountTypeDTO } from '@/api/models/accountTypes';
import { accountTypesService } from '@/api/services/accountTypes';

interface AccountTypesState {
  accountTypes: AccountTypeDTO[];
  loading: boolean;
  error: string | null;
}

export const useAccountTypesStore = defineStore('accountTypes', {
  state: (): AccountTypesState => ({
    accountTypes: [],
    loading: false,
    error: null
  }),

  actions: {
    async fetchAccountTypes() {
      this.loading = true;
      this.error = null;
      try {
        this.accountTypes = await accountTypesService.getAccountTypes();
        return this.accountTypes;
      } catch (err: any) {
        this.error = err.message;
        throw err;
      } finally {
        this.loading = false;
      }
    },

    clearError() {
      this.error = null;
    }
  }
}); 