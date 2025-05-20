import { mount } from '@vue/test-utils';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import StatisticsView from '@/views/StatisticsView.vue';
import { createTestingPinia } from '@pinia/testing';
import { useStatisticsStore } from '@/store/modules/statistics';
import type { StatisticsDTO } from '@/api/models/statistics';

// Mock statistics data
const mockStatistics: StatisticsDTO = {
  totalIncome: 20000,
  totalExpense: 10000,
  accountBalances: [
    {
      accountId: 1,
      accountName: '工商银行',
      balance: 10000
    },
    {
      accountId: 2,
      accountName: '建设银行',
      balance: 10000
    }
  ],
  monthlyStatistics: [
    {
      month: '2024-03',
      income: 10000,
      expense: 5000
    },
    {
      month: '2024-02',
      income: 10000,
      expense: 5000
    }
  ]
};

describe('StatisticsView', () => {
  let wrapper: any;
  let statisticsStore: any;

  beforeEach(() => {
    const pinia = createTestingPinia({
      createSpy: vi.fn,
      initialState: {
        statistics: {
          statistics: null,
          loading: false,
          error: null
        }
      }
    });

    statisticsStore = useStatisticsStore(pinia);
    statisticsStore.fetchStatistics = vi.fn().mockResolvedValue(mockStatistics);

    wrapper = mount(StatisticsView, {
      global: {
        plugins: [pinia]
      }
    });
  });

  it('should render statistics content', async () => {
    statisticsStore.statistics = mockStatistics;
    await wrapper.vm.$nextTick();
    expect(wrapper.find('.statistics-content').exists()).toBe(true);
  });

  it('should fetch statistics on mount', async () => {
    await wrapper.vm.$nextTick();
    expect(statisticsStore.fetchStatistics).toHaveBeenCalled();
  });

  it('renders summary correctly', async () => {
    statisticsStore.statistics = mockStatistics;
    await wrapper.vm.$nextTick();
    expect(wrapper.find('h1').text()).toBe('统计');
    const summaryItems = wrapper.findAll('.summary-item');
    expect(summaryItems.length).toBe(3);
    expect(summaryItems[0].find('h3').text()).toBe('总收入');
    expect(summaryItems[1].find('h3').text()).toBe('总支出');
    expect(summaryItems[2].find('h3').text()).toBe('净收入');
  });

  it('displays amounts correctly', async () => {
    statisticsStore.statistics = mockStatistics;
    await wrapper.vm.$nextTick();
    const amounts = wrapper.findAll('.amount');
    expect(amounts[0].text()).toBe('+20000');
    expect(amounts[1].text()).toBe('-10000');
    expect(amounts[2].text()).toBe('+10000');
  });

  it('renders charts correctly', async () => {
    statisticsStore.statistics = mockStatistics;
    await wrapper.vm.$nextTick();
    const charts = wrapper.findAll('.chart');
    expect(charts.length).toBe(2);
    expect(charts[0].find('h3').text()).toBe('按类型统计');
    expect(charts[1].find('h3').text()).toBe('按账户统计');
  });

  it('handles error state correctly', async () => {
    statisticsStore.error = '加载失败';
    await wrapper.vm.$nextTick();
    const errorElement = wrapper.find('.error');
    expect(errorElement.exists()).toBe(true);
    expect(errorElement.text()).toBe('加载失败');
  });

  it('updates statistics when changing date range', async () => {
    const startDateInput = wrapper.find('input[type="date"]');
    const endDateInput = wrapper.findAll('input[type="date"]')[1];
    const queryButton = wrapper.find('button');

    await startDateInput.setValue('2024-03-01');
    await endDateInput.setValue('2024-03-31');
    await queryButton.trigger('click');

    expect(statisticsStore.fetchStatistics).toHaveBeenCalledWith('2024-03-01', '2024-03-31');
  });

  it('displays loading state', async () => {
    statisticsStore.loading = true;
    await wrapper.vm.$nextTick();
    expect(wrapper.find('.loading').exists()).toBe(true);
    expect(wrapper.find('.loading').text()).toBe('加载中...');
  });
}); 