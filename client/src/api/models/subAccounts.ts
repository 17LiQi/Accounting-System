export interface SubAccountDTO {
  subAccountId: number;
  accountId: number;
  name: string;
  balance: number;
  description?: string;
}

export interface SubAccountRequest {
  name: string;
  balance: number;
  description?: string;
} 