import { mount } from '@vue/test-utils';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import SubAccountsView from '@/views/SubAccountsView.vue';
import { createTestingPinia } from '@pinia/testing';
import { useSubAccountsStore } from '@/store/modules/subAccounts';
import { useAccountStore } from '@/store/modules/accounts';
import { SubAccountDTO } from '@/api/models/subAccounts';
import { AccountDTO } from '@/api/models/accounts';
import { AccountRequestTypeEnum } from '@/api/models/accounts';

// Mock accounts data
const mockAccounts: AccountDTO[] = [
  {
    accountId: 1,
    accountName: '工商银行',
    accountType: AccountRequestTypeEnum.Bank,
    balance: 10000,
    description: '工商银行储蓄卡'
  }
];

// Mock sub accounts data
const mockSubAccounts: SubAccountDTO[] = [
  {
    subAccountId: 1,
    name: '工资卡',
    balance: 5000,
    accountId: 1
  },
  {
    subAccountId: 2,
    name: '储蓄卡',
    balance: 5000,
    accountId: 1
  }
];

describe('SubAccountsView', () => {
  let wrapper: any;
  let subAccountsStore: any;
  let accountStore: any;

  beforeEach(() => {
    const pinia = createTestingPinia({
      createSpy: vi.fn,
      initialState: {
        subAccounts: {
          subAccounts: [],
          loading: false,
          error: null
        },
        accounts: {
          accounts: [],
          loading: false,
          error: null
        }
      }
    });

    subAccountsStore = useSubAccountsStore(pinia);
    accountStore = useAccountStore(pinia);

    subAccountsStore.fetchSubAccounts = vi.fn().mockResolvedValue(mockSubAccounts);
    accountStore.fetchAccounts = vi.fn().mockResolvedValue(mockAccounts);
    subAccountsStore.deleteSubAccount = vi.fn().mockResolvedValue(undefined);

    wrapper = mount(SubAccountsView, {
      global: {
        plugins: [pinia]
      }
    });
  });

  it('should render sub accounts list', async () => {
    accountStore.accounts = mockAccounts;
    subAccountsStore.subAccounts = mockSubAccounts;
    await wrapper.vm.$nextTick();
    expect(wrapper.find('.sub-accounts-list').exists()).toBe(true);
  });

  it('should fetch accounts and sub accounts on mount', async () => {
    await wrapper.vm.$nextTick();
    expect(accountStore.fetchAccounts).toHaveBeenCalled();
    expect(subAccountsStore.fetchSubAccounts).toHaveBeenCalled();
  });

  it('renders sub account list correctly', async () => {
    accountStore.accounts = mockAccounts;
    subAccountsStore.subAccounts = mockSubAccounts;
    await wrapper.vm.$nextTick();
    expect(wrapper.find('h1').text()).toBe('子账户管理');
    const subAccountCards = wrapper.findAll('.sub-account-card');
    expect(subAccountCards.length).toBe(2);
    expect(subAccountCards[0].find('h3').text()).toBe('工资卡');
    expect(subAccountCards[1].find('h3').text()).toBe('储蓄卡');
  });

  it('shows create dialog when clicking add button', async () => {
    const addButton = wrapper.find('button');
    await addButton.trigger('click');
    await wrapper.vm.$nextTick();
    expect(wrapper.find('.dialog').exists()).toBe(true);
    expect(wrapper.find('.dialog h2').text()).toBe('添加子账户');
  });

  it('shows edit dialog when clicking edit button', async () => {
    subAccountsStore.subAccounts = mockSubAccounts;
    await wrapper.vm.$nextTick();
    const editButtons = wrapper.findAll('.sub-account-card button');
    expect(editButtons.length).toBeGreaterThan(0);
    await editButtons[0].trigger('click');
    await wrapper.vm.$nextTick();
    expect(wrapper.find('.dialog').exists()).toBe(true);
    expect(wrapper.find('.dialog h2').text()).toBe('编辑子账户');
  });

  it('deletes sub account when clicking delete button', async () => {
    subAccountsStore.subAccounts = mockSubAccounts;
    await wrapper.vm.$nextTick();
    const deleteButtons = wrapper.findAll('.sub-account-card button.delete');
    expect(deleteButtons.length).toBeGreaterThan(0);
    window.confirm = vi.fn(() => true);
    await deleteButtons[0].trigger('click');
    expect(subAccountsStore.deleteSubAccount).toHaveBeenCalledWith(1, 1);
  });

  it('handles error state correctly', async () => {
    // 模拟 fetchAccounts 报错
    accountStore.fetchAccounts = vi.fn().mockRejectedValue(new Error('加载失败'));

    // 重新挂载组件以触发 onMounted 中的 fetchAccounts
    wrapper = mount(SubAccountsView, {
      global: {
        plugins: [wrapper.vm.$.appContext.app._context.provides.pinia] // 使用相同的 Pinia 实例
      }
    });

    await wrapper.vm.$nextTick(); // 等待 nextTick
    await Promise.resolve(); // 等待 Promise 拒绝被处理

    const errorElement = wrapper.find('.error');
    expect(errorElement.exists()).toBe(true);
    expect(errorElement.text()).toBe('加载失败');
  });


  it('updates sub account list when changing parent account', async () => {
    accountStore.accounts = mockAccounts;
    await wrapper.vm.$nextTick();
    const select = wrapper.find('select');
    await select.setValue(1);
    await wrapper.vm.$nextTick();
    expect(subAccountsStore.fetchSubAccounts).toHaveBeenCalledWith(1);
  });
}); 