import type { SubAccountDTO } from '../sub-accounts/sub-account-dto';

export interface AccountDTO {
  accountId: number;
  accountName: string;
  typeId: number;
  type: string;
  userId: number;
  username: string;
  description?: string;
  subAccounts?: SubAccountDTO[];
}

export interface AccountDTO {
  accountId: number;
  accountName: string;
  typeId: number;
  type: string;
  description?: string;
}