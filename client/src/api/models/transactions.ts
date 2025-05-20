export interface TransactionDTO {
  transactionId: number;
  accountId: number;
  typeId: number;
  amount: number;
  description: string;
  transactionDate: string;
}

export interface TransactionRequest {
  accountId: number;
  typeId: number;
  amount: number;
  description: string;
  transactionDate: string;
} 