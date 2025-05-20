import { defineStore } from 'pinia';
import { transactionTypesService } from '@/api/services/transactionTypes';
import type { TransactionTypeDTO } from '@/api/models/transactionTypes';

interface TransactionTypesState {
  types: TransactionTypeDTO[];
  loading: boolean;
  error: string | null;
}

export const useTransactionTypesStore = defineStore('transactionTypes', {
  state: (): TransactionTypesState => ({
    types: [],
    loading: false,
    error: null
  }),

  actions: {
    async getTransactionTypes() {
      this.loading = true;
      this.error = null;
      try {
        const types = await transactionTypesService.getTransactionTypes();
        this.types = types;
        return types;
      } catch (error: any) {
        this.error = error.message || '获取交易类型失败';
        throw error;
      } finally {
        this.loading = false;
      }
    }
  }
}); 