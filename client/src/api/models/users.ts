export interface UserDTO {
  userId: number;
  username: string;
  role: string;
  email: string;
}

export interface UserRequest {
  username: string;
  password: string;
  role: string;
  email: string;
} 