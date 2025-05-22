export interface LoginResponse {
    token: string;
    user: {
        userId: number;
        username: string;
        role: 'ADMIN' | 'USER';
        email?: string;
        phone?: string;
    };
} 