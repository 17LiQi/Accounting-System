import { mount } from '@vue/test-utils';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import AccountsView from '@/views/AccountsView.vue';
import { createTestingPinia } from '@pinia/testing';
import { useAccountStore } from '@/store/modules/accounts';
import { useAccountTypesStore } from '@/store/modules/accountTypes';
import { AccountDTO } from '@/api/models/accounts';
import { AccountTypeDTO } from '@/api/models/accountTypes';
import { AccountRequestTypeEnum } from '@/api/models/accounts';

// Mock accounts data
const mockAccounts: AccountDTO[] = [
  {
    accountId: 1,
    accountName: '工商银行',
    accountType: AccountRequestTypeEnum.Bank,
    balance: 10000,
    description: '工商银行储蓄卡'
  },
  {
    accountId: 2,
    accountName: '建设银行',
    accountType: AccountRequestTypeEnum.Bank,
    balance: 20000,
    description: '建设银行储蓄卡'
  }
];

// Mock account types
const mockAccountTypes: AccountTypeDTO[] = [
  {
    typeId: 1,
    typeName: '银行账户'
  }
];

describe('AccountsView', () => {
  let wrapper: any;
  let accountStore: any;
  let accountTypesStore: any;

  beforeEach(() => {
    // 创建 Pinia store
    const pinia = createTestingPinia({
      createSpy: vi.fn,
      initialState: {
        accounts: {
          accounts: [],
          loading: false,
          error: null
        },
        accountTypes: {
          accountTypes: [],
          loading: false,
          error: null
        }
      }
    });

    // 获取 store 实例
    accountStore = useAccountStore(pinia);
    accountTypesStore = useAccountTypesStore(pinia);

    // 模拟 store 方法
    accountStore.fetchAccounts = vi.fn().mockResolvedValue(mockAccounts);
    accountTypesStore.fetchAccountTypes = vi.fn().mockResolvedValue(mockAccountTypes);
    accountStore.deleteAccount = vi.fn().mockResolvedValue(undefined);

    // 挂载组件
    wrapper = mount(AccountsView, {
      global: {
        plugins: [pinia]
      }
    });
  });

  it('should render accounts list', async () => {
    accountStore.accounts = mockAccounts;
    await wrapper.vm.$nextTick();
    expect(wrapper.find('.accounts-list').exists()).toBe(true);
  });

  it('should fetch accounts and types on mount', async () => {
    // 创建新的 Pinia 实例，确保初始状态为空
    const pinia = createTestingPinia({
      createSpy: vi.fn,
      initialState: {
        accounts: {
          accounts: [],
          loading: false,
          error: null
        },
        accountTypes: {
          accountTypes: [],
          loading: false,
          error: null
        }
      }
    });

    const accountStore = useAccountStore(pinia);
    const accountTypesStore = useAccountTypesStore(pinia);

    // 模拟 fetch 方法
    const mockFetchAccounts = vi.fn().mockResolvedValue(mockAccounts);
    const mockFetchAccountTypes = vi.fn().mockResolvedValue(mockAccountTypes);
    accountStore.fetchAccounts = mockFetchAccounts;
    accountTypesStore.fetchAccountTypes = mockFetchAccountTypes;

    // 挂载组件
    const wrapper = mount(AccountsView, {
      global: {
        plugins: [pinia]
      }
    });

    // 等待异步操作完成
    await wrapper.vm.$nextTick();
    await new Promise(resolve => setTimeout(resolve, 0)); // 等待 Promise 解析

    // 验证 fetch 方法是否被调用
    expect(mockFetchAccounts).toHaveBeenCalled();
    expect(mockFetchAccountTypes).toHaveBeenCalled();
  });

  it('renders account list correctly', async () => {
    accountStore.accounts = mockAccounts;
    await wrapper.vm.$nextTick();
    expect(wrapper.find('h1').text()).toBe('账户管理');
    const accountCards = wrapper.findAll('.account-card');
    expect(accountCards.length).toBe(2);
    expect(accountCards[0].find('h3').text()).toBe('工商银行');
    expect(accountCards[1].find('h3').text()).toBe('建设银行');
  });

  it('shows create dialog when clicking add button', async () => {
    const addButton = wrapper.find('button.add-account');
    await addButton.trigger('click');
    await wrapper.vm.$nextTick();
    expect(wrapper.find('.dialog').exists()).toBe(true);
    expect(wrapper.find('.dialog h2').text()).toBe('添加账户');
  });

  it('shows edit dialog when clicking edit button', async () => {
    accountStore.accounts = mockAccounts;
    await wrapper.vm.$nextTick();
    const editButtons = wrapper.findAll('.account-card button.edit');
    expect(editButtons.length).toBeGreaterThan(0);
    await editButtons[0].trigger('click');
    await wrapper.vm.$nextTick();
    expect(wrapper.find('.dialog').exists()).toBe(true);
    expect(wrapper.find('.dialog h2').text()).toBe('编辑账户');
  });

  it('deletes account when clicking delete button', async () => {
    accountStore.accounts = mockAccounts;
    await wrapper.vm.$nextTick();
    const deleteButtons = wrapper.findAll('.account-card button.delete');
    expect(deleteButtons.length).toBeGreaterThan(0);
    window.confirm = vi.fn(() => true);
    await deleteButtons[0].trigger('click');
    expect(accountStore.deleteAccount).toHaveBeenCalledWith(1);
  });

  it('handles error state correctly', async () => {
    // 创建新的 Pinia 实例
    const pinia = createTestingPinia({
      createSpy: vi.fn,
      initialState: {
        accounts: {
          accounts: [],
          loading: false,
          error: '加载失败'
        },
        accountTypes: {
          accountTypes: mockAccountTypes,
          loading: false,
          error: null
        }
      }
    });

    // 挂载组件
    wrapper = mount(AccountsView, {
      global: {
        plugins: [pinia]
      }
    });

    // 等待组件更新
    await wrapper.vm.$nextTick();

    // 验证错误状态
    const errorElement = wrapper.find('.error-message');
    expect(errorElement.exists()).toBe(true);
    expect(errorElement.text()).toBe('加载失败');
  });
});