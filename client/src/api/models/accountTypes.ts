export interface AccountTypeDTO {
  typeId: number;
  typeName: string;
  description?: string;
}

export interface AccountTypeRequest {
  typeName: string;
  description?: string;
} 