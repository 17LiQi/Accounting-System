export interface AccountType {
  typeId: number;
  typeName: string;
}

export interface Account {
  accountId: number;
  accountName: string;
  accountType: AccountType;
  balance: number;
}

export interface AccountRequest {
  accountName: string;
  typeId: number;
  type: string;
}

export interface Transaction {
  id: string;
  date: string;
  type: string;
  amount: number;
  description: string;
} 