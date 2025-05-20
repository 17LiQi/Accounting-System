import { vi } from 'vitest';
import type { AxiosResponse } from 'axios';
import type { UserDTO, UserRequest } from '@/api/models/users';

export const mockUsersApi = {
  getUsers: vi.fn().mockImplementation((): Promise<AxiosResponse<UserDTO[]>> => {
    return Promise.resolve({
      data: [
        {
          userId: 1,
          username: 'test1',
          email: 'test1@example.com',
          role: 'user'
        },
        {
          userId: 2,
          username: 'test2',
          email: 'test2@example.com',
          role: 'admin'
        }
      ],
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  getUser: vi.fn().mockImplementation((id: number): Promise<AxiosResponse<UserDTO>> => {
    return Promise.resolve({
      data: {
        userId: id,
        username: 'test',
        email: 'test@example.com',
        role: 'user'
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  createUser: vi.fn().mockImplementation((request: UserRequest): Promise<AxiosResponse<UserDTO>> => {
    return Promise.resolve({
      data: {
        userId: 3,
        username: request.username,
        email: request.email,
        role: request.role
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  updateUser: vi.fn().mockImplementation((id: number, request: UserRequest): Promise<AxiosResponse<UserDTO>> => {
    return Promise.resolve({
      data: {
        userId: id,
        username: request.username,
        email: request.email,
        role: request.role
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  deleteUser: vi.fn().mockImplementation((): Promise<AxiosResponse<null>> => {
    return Promise.resolve({
      data: null,
      status: 204,
      statusText: 'No Content',
      headers: {},
      config: {} as any
    });
  })
}; 