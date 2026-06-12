import { request } from '@umijs/max';

export type UserRole = 'admin' | 'operator';
export type UserStatus = 'active' | 'disabled';

export interface User {
  id: string;
  username: string;
  role: UserRole;
  status: UserStatus;
  createdAt: string;
  updatedAt: string;
}

export interface UserListParams {
  keyword?: string;
  page?: number;
  pageSize?: number;
}

export interface UserListResponse {
  list: User[];
  total: number;
}

export interface CreateUserRequest {
  username: string;
  password: string;
  role: UserRole;
}

export interface UpdateUserRequest {
  username?: string;
  role?: UserRole;
  status?: UserStatus;
}

export interface ResetPasswordRequest {
  oldPassword?: string;
  newPassword: string;
}

interface ApiResponse<T> {
  code: number;
  message?: string;
  data: T;
}

export async function listUsers(params: UserListParams) {
  return request<ApiResponse<UserListResponse>>('/api/v1/users', {
    method: 'GET',
    params,
  });
}

export async function createUser(data: CreateUserRequest) {
  return request<ApiResponse<User>>('/api/v1/users', {
    method: 'POST',
    data,
  });
}

export async function updateUser(id: string, data: UpdateUserRequest) {
  return request<ApiResponse<User>>(`/api/v1/users/${id}`, {
    method: 'PUT',
    data,
  });
}

export async function deleteUser(id: string) {
  return request(`/api/v1/users/${id}`, {
    method: 'DELETE',
  });
}

export async function resetPassword(id: string, data: ResetPasswordRequest) {
  return request<ApiResponse<{ message: string }>>(
    `/api/v1/users/${id}/password`,
    {
      method: 'PUT',
      data,
    },
  );
}
