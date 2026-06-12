import { request } from '@umijs/max';

export type MenuStatus = 'enabled' | 'disabled';

export interface Menu {
	id: string;
	name: string;
	path: string;
	icon?: string;
	parentId?: string;
	sort: number;
	status: MenuStatus;
	createdAt: string;
	updatedAt: string;
	children?: Menu[];
}

export interface MenuListParams {
	keyword?: string;
	tree?: boolean;
}

export interface MenuListResponse {
	list: Menu[];
	total: number;
}

export interface MenuMutationRequest {
	name: string;
	path: string;
	icon?: string;
	parentId?: string;
	sort: number;
	status: MenuStatus;
}

interface ApiResponse<T> {
	code: number;
	message?: string;
	data: T;
}

export async function listMenus(params: MenuListParams = {}) {
	return request<ApiResponse<MenuListResponse>>('/api/v1/menus', {
		method: 'GET',
		params,
	});
}

export async function getMenu(id: string) {
	return request<ApiResponse<Menu>>(`/api/v1/menus/${id}`, {
		method: 'GET',
	});
}

export async function createMenu(data: MenuMutationRequest) {
	return request<ApiResponse<Menu>>('/api/v1/menus', {
		method: 'POST',
		data,
	});
}

export async function updateMenu(id: string, data: MenuMutationRequest) {
	return request<ApiResponse<Menu>>(`/api/v1/menus/${id}`, {
		method: 'PUT',
		data,
	});
}

export async function deleteMenu(id: string) {
	return request(`/api/v1/menus/${id}`, {
		method: 'DELETE',
	});
}

export async function updateMenuStatus(id: string, status: MenuStatus) {
	return request<ApiResponse<Menu>>(`/api/v1/menus/${id}/status`, {
		method: 'PUT',
		data: { status },
	});
}
