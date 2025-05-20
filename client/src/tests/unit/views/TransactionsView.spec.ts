import { mount } from '@vue/test-utils';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import TransactionsView from '@/views/TransactionsView.vue';
import { createTestingPinia } from '@pinia/testing';
import { useTransactionStore } from '@/store/modules/transactions';
import { useTransactionTypesStore } from '@/store/modules/transactionTypes';
import { TransactionDTO } from '@/api/models/transactions';
import { TransactionTypeDTO } from '@/api/models/transactionTypes';

// Mock transactions data
const mockTransactions: TransactionDTO[] = [
  {
    transactionId: 1,
    amount: 10000,
    typeId: 1,
    description: '工资收入',
    transactionDate: '2024-01-01',
    accountId: 1
  },
  {
    transactionId: 2,
    amount: 1000,
    typeId: 2,
    description: '购物支出',
    transactionDate: '2024-01-02',
    accountId: 1
  }
];

// Mock transaction types
const mockTransactionTypes: TransactionTypeDTO[] = [
  {
    typeId: 1,
    typeName: '收入'
  },
  {
    typeId: 2,
    typeName: '支出'
  }
];

describe('TransactionsView', () => {
  let wrapper: any;
  let transactionStore: any;
  let transactionTypesStore: any;

  beforeEach(() => {
    const pinia = createTestingPinia({
      createSpy: vi.fn,
      initialState: {
        transactions: {
          transactions: [],
          loading: false,
          error: null
        },
        transactionTypes: {
          transactionTypes: [],
          loading: false,
          error: null
        }
      }
    });

    transactionStore = useTransactionStore(pinia);
    transactionTypesStore = useTransactionTypesStore(pinia);

    transactionStore.fetchTransactions = vi.fn().mockResolvedValue(mockTransactions);
    transactionTypesStore.getTransactionTypes = vi.fn().mockResolvedValue(mockTransactionTypes);
    transactionStore.deleteTransaction = vi.fn().mockResolvedValue(undefined);

    wrapper = mount(TransactionsView, {
      global: {
        plugins: [pinia]
      }
    });
  });

  it('should render transactions list', async () => {
    transactionStore.transactions = mockTransactions;
    await wrapper.vm.$nextTick();
    expect(wrapper.find('.transactions-list').exists()).toBe(true);
  });

  it('should fetch transactions and types on mount', async () => {
    await wrapper.vm.$nextTick();
    await new Promise(resolve => setTimeout(resolve, 0));
    expect(transactionStore.fetchTransactions).toHaveBeenCalled();
    expect(transactionTypesStore.getTransactionTypes).toHaveBeenCalled();
  });

  it('renders transaction list correctly', async () => {
    transactionStore.transactions = mockTransactions;
    await wrapper.vm.$nextTick();
    expect(wrapper.find('h1').text()).toBe('交易记录');
    const transactionCards = wrapper.findAll('.transaction-card');
    expect(transactionCards.length).toBe(2);
    expect(transactionCards[0].find('h3').text()).toBe('工资收入');
    expect(transactionCards[1].find('h3').text()).toBe('购物支出');
  });

  it('shows edit dialog when clicking edit button', async () => {
    transactionStore.transactions = mockTransactions;
    await wrapper.vm.$nextTick();
    const editButtons = wrapper.findAll('.transaction-card button.edit');
    expect(editButtons.length).toBeGreaterThan(0);
    await editButtons[0].trigger('click');
    await wrapper.vm.$nextTick();
    expect(wrapper.find('.dialog').exists()).toBe(true);
    expect(wrapper.find('.dialog h2').text()).toBe('编辑交易');
  });

  it('deletes transaction when clicking delete button', async () => {
    transactionStore.transactions = mockTransactions;
    await wrapper.vm.$nextTick();
    const deleteButtons = wrapper.findAll('.transaction-card button.delete');
    expect(deleteButtons.length).toBeGreaterThan(0);
    window.confirm = vi.fn(() => true);
    await deleteButtons[0].trigger('click');
    expect(transactionStore.deleteTransaction).toHaveBeenCalledWith(1);
  });

  it('handles error state correctly', async () => {
    const errorPinia = createTestingPinia({
      createSpy: vi.fn
    });

    const failingStore = useTransactionStore(errorPinia);
    const typeStore = useTransactionTypesStore(errorPinia);

    failingStore.fetchTransactions = vi.fn().mockRejectedValue(new Error('加载失败'));
    typeStore.getTransactionTypes = vi.fn().mockResolvedValue(mockTransactionTypes);

    const errorWrapper = mount(TransactionsView, {
      global: {
        plugins: [errorPinia]
      }
    });

    await new Promise(resolve => setTimeout(resolve, 0)); // 等待 Promise 完成

    const errorElement = errorWrapper.find('.error');
    expect(errorElement.exists()).toBe(true);
    expect(errorElement.text()).toBe('加载失败');
  });

  it('displays transaction amounts correctly', async () => {
    transactionStore.transactions = mockTransactions;
    await wrapper.vm.$nextTick();
    const amountElements = wrapper.findAll('.transaction-card .amount');
    expect(amountElements[0].text()).toBe('10000');
    expect(amountElements[1].text()).toBe('1000');
  });

  it('displays transaction types correctly', async () => {
    transactionStore.transactions = mockTransactions;
    await wrapper.vm.$nextTick();
    const transactionCards = wrapper.findAll('.transaction-card');
    expect(transactionCards[0].findAll('p')[1].text()).toBe('类型: 收入');
    expect(transactionCards[1].findAll('p')[1].text()).toBe('类型: 支出');
  });
}); 