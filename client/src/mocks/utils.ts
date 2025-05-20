import { AxiosResponse } from 'axios';

export function mockApiResponse<T>(data: T, status = 200): AxiosResponse<T> {
  return {
    data,
    status,
    statusText: status === 200 ? 'OK' : 'No Content',
    headers: {},
    config: {} as any
  };
} 