import type { AccountRequestTypeEnum } from './accounts/account-request';
import type { SubAccountDTO } from './sub-accounts/sub-account-dto';

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

export interface AccountType {
  typeId: number;
  typeName: string;
}

export enum AccountTypeEnum {
  CASH = '现金',
  BANK = '银行卡',
  ALIPAY = '支付宝',
  WECHAT = '微信'
}

export type { AccountRequest } from './accounts/account-request';
export { AccountRequestTypeEnum } from './accounts/account-request'; 