export interface TransactionTypeDTO {
  typeId: number;
  typeName: string;
  description?: string;
}

export interface TransactionTypeRequest {
  typeName: string;
  description?: string;
} 