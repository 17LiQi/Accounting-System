import { defineStore } from 'pinia';
import type { SubAccountDTO } from '@/api/models/sub-accounts/sub-account-dto';
import type { SubAccountRequest } from '@/api/models/sub-accounts/sub-account-request';
import { subAccountsService } from '@/api/services/subAccounts';

export const useSubAccountsStore = defineStore('subAccounts', {
  state: () => ({
    subAccounts: [] as SubAccountDTO[],
    currentSubAccount: null as SubAccountDTO | null,
    loading: false,
    error: null as string | null
  }),

  actions: {
    async fetchSubAccounts(accountId: number) {
      this.loading = true;
      try {
        const subAccounts = await subAccountsService.getSubAccounts(accountId);
        this.subAccounts = subAccounts;
        return subAccounts;
      } catch (error) {
        this.error = error instanceof Error ? error.message : '获取子账户失败';
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async createSubAccount(accountId: number, request: SubAccountRequest) {
      this.loading = true;
      try {
        const subAccount = await subAccountsService.createSubAccount(accountId, request);
        this.subAccounts.push(subAccount);
        return subAccount;
      } catch (error) {
        this.error = error instanceof Error ? error.message : '创建子账户失败';
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async updateSubAccount(accountId: number, subAccountId: number, request: SubAccountRequest) {
      this.loading = true;
      try {
        const subAccount = await subAccountsService.updateSubAccount(accountId, subAccountId, request);
        const index = this.subAccounts.findIndex(sa => sa.subAccountId === subAccountId);
        if (index !== -1) {
          this.subAccounts[index] = subAccount;
        }
        if (this.currentSubAccount?.subAccountId === subAccountId) {
          this.currentSubAccount = subAccount;
        }
        return subAccount;
      } catch (error) {
        this.error = error instanceof Error ? error.message : '更新子账户失败';
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async deleteSubAccount(accountId: number, subAccountId: number) {
      this.loading = true;
      try {
        await subAccountsService.deleteSubAccount(accountId, subAccountId);
        this.subAccounts = this.subAccounts.filter(sa => sa.subAccountId !== subAccountId);
        if (this.currentSubAccount?.subAccountId === subAccountId) {
          this.currentSubAccount = null;
        }
      } catch (error) {
        this.error = error instanceof Error ? error.message : '删除子账户失败';
        throw error;
      } finally {
        this.loading = false;
      }
    }
  }
}); 