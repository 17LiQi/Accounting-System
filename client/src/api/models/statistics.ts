export interface StatisticsDTO {
  totalIncome: number;
  totalExpense: number;
  accountBalances: {
    accountId: number;
    accountName: string;
    balance: number;
  }[];
  monthlyStatistics: {
    month: string;
    income: number;
    expense: number;
  }[];
} 