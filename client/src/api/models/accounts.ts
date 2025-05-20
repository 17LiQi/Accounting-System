import type { AccountRequestTypeEnum } from './accounts/account-request';

export interface AccountDTO {
  accountId: number;
  accountName: string;
  accountType: AccountRequestTypeEnum;
  description?: string;
  balance: number;
  createdAt?: string;
  updatedAt?: string;
}

export type { AccountRequest } from './accounts/account-request';
export { AccountRequestTypeEnum } from './accounts/account-request'; 