export interface TransactionQueryParams {
  page: number;
  size: number;
  sort: 'asc' | 'desc';
  userId?: number;
  subAccountId?: number;
  startDate?: string;
  endDate?: string;
} 