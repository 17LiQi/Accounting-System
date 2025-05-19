// src/store/modules/accounts.ts
import { defineStore } from 'pinia';
import { AccountsApi } from '@/api/apis/accounts-api';
import type { Account } from '@/api/models/accounts/account';
import type { AccountRequest } from '@/api/models/accounts/account-request';
import { apiClient } from '@/api/client';
import axios from 'axios';
import { mockAccountsApi } from '@/mocks/accounts';
import { isUsingMock } from '@/api/client';

export const useAccountStore = defineStore('accounts', {
    state: () => ({
        accounts: [] as Account[],
        currentAccount: null as Account | null,
        loading: false,
        error: null as string | null,
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
                if (isUsingMock) {
                    const response = await mockAccountsApi.list();
                    this.accounts = response.data;
                } else {
                    const api = new AccountsApi(apiClient);
                    const response = await api.accountsList();
                    this.accounts = Array.isArray(response.data) ? response.data : [response.data];
                }
            } catch (error) {
                console.error('获取账户列表失败:', error);
                if (axios.isAxiosError(error)) {
                    if (error.response?.status === 401) {
                        this.error = '未授权，请重新登录';
                    } else if (error.response?.status === 404) {
                        this.error = 'API 端点不存在';
                    } else if (error.code === 'ECONNREFUSED') {
                        this.error = '无法连接到服务器，请检查服务器是否运行';
                    } else {
                        this.error = `获取账户列表失败: ${error.response?.data?.message || error.message}`;
                    }
                } else {
                    this.error = '获取账户列表失败';
                }
            } finally {
                this.loading = false;
            }
        },
        async createAccount(request: AccountRequest) {
            this.loading = true;
            this.error = null;
            try {
                if (isUsingMock) {
                    const response = await mockAccountsApi.create(request);
                    this.accounts.push(response.data);
                    return response.data;
                } else {
                    const api = new AccountsApi(apiClient);
                    const response = await api.accountsCreate(request);
                    this.accounts.push(response.data);
                    return response.data;
                }
            } catch (error) {
                console.error('创建账户失败:', error);
                if (axios.isAxiosError(error)) {
                    if (error.response?.status === 401) {
                        this.error = '未授权，请重新登录';
                    } else if (error.response?.status === 400) {
                        this.error = `创建账户失败: ${error.response.data?.message || '请求数据无效'}`;
                    } else if (error.code === 'ECONNREFUSED') {
                        this.error = '无法连接到服务器，请检查服务器是否运行';
                    } else {
                        this.error = `创建账户失败: ${error.response?.data?.message || error.message}`;
                    }
                } else {
                    this.error = '创建账户失败';
                }
                throw error;
            } finally {
                this.loading = false;
            }
        },
        async updateAccount(id: number, request: AccountRequest) {
            this.loading = true;
            this.error = null;
            try {
                if (isUsingMock) {
                    const response = await mockAccountsApi.update(id.toString(), request);
                    const index = this.accounts.findIndex(account => account.accountId === id);
                    if (index !== -1) {
                        this.accounts[index] = response.data;
                    }
                    return response.data;
                } else {
                    const api = new AccountsApi(apiClient);
                    const response = await api.accountsUpdate(id.toString(), request);
                    const index = this.accounts.findIndex(account => account.accountId === id);
                    if (index !== -1) {
                        this.accounts[index] = response.data;
                    }
                    return response.data;
                }
            } catch (error) {
                console.error('更新账户失败:', error);
                if (axios.isAxiosError(error)) {
                    if (error.response?.status === 401) {
                        this.error = '未授权，请重新登录';
                    } else if (error.response?.status === 404) {
                        this.error = '账户不存在';
                    } else if (error.code === 'ECONNREFUSED') {
                        this.error = '无法连接到服务器，请检查服务器是否运行';
                    } else {
                        this.error = `更新账户失败: ${error.response?.data?.message || error.message}`;
                    }
                } else {
                    this.error = '更新账户失败';
                }
                throw error;
            } finally {
                this.loading = false;
            }
        },
        async deleteAccount(id: number) {
            this.loading = true;
            this.error = null;
            try {
                if (isUsingMock) {
                    await mockAccountsApi.delete(id.toString());
                    this.accounts = this.accounts.filter(account => account.accountId !== id);
                } else {
                    const api = new AccountsApi(apiClient);
                    await api.accountsDelete(id.toString());
                    this.accounts = this.accounts.filter(account => account.accountId !== id);
                }
            } catch (error) {
                console.error('删除账户失败:', error);
                if (axios.isAxiosError(error)) {
                    if (error.response?.status === 401) {
                        this.error = '未授权，请重新登录';
                    } else if (error.response?.status === 404) {
                        this.error = '账户不存在';
                    } else if (error.code === 'ECONNREFUSED') {
                        this.error = '无法连接到服务器，请检查服务器是否运行';
                    } else {
                        this.error = `删除账户失败: ${error.response?.data?.message || error.message}`;
                    }
                } else {
                    this.error = '删除账户失败';
                }
                throw error;
            } finally {
                this.loading = false;
            }
        },
        setCurrentAccount(account: Account | null) {
            this.currentAccount = account;
        },
        clearError() {
            this.error = null;
        },
    },
});