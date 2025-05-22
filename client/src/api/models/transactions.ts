export interface TransactionDTO {
  transactionId: number;
  time: string;
  typeId: number;
  isIncome: boolean;
  subAccountId: number;
  amount: string;
  userId: number;
  remarks?: string;
}

export interface TransactionRequest {
  time: string;
  typeId: number;
  subAccountId: number;
  amount: string;
  remarks?: string;
} 