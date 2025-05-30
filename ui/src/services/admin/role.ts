/* eslint-disable */
import {request} from '@umijs/max';

/** 修改角色 修改角色 PUT /v3/roles */
export async function modifyRole(body: API.RoleModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/roles', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存角色 保存角色 POST /v3/roles */
export async function saveRole(body: API.RoleSaveCmd,requestId: string, options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/roles', {
		method: 'POST',
		headers: {
			'request-id': requestId,
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除角色 删除角色 DELETE /v3/roles */
export async function removeRole(body: number[], options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/roles', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看角色详情 查看角色详情 GET /v3/roles/${param0} */
export async function getRoleById(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdParams,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/api/admin/v3/roles/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出角色 导出角色 POST /v3/roles/export */
export async function exportRole(body: API.RoleExportCmd, options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/roles/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入角色 导入角色 POST /v3/roles/import */
export async function importRole(body: {}, file?: File[], options?: { [key: string]: any }) {
	const formData = new FormData();

	if (file) {
		file.forEach((f) => formData.append('file', f || ''));
	}

	Object.keys(body).forEach((ele) => {
		const item = (body as any)[ele];

		if (item !== undefined && item !== null) {
			if (typeof item === 'object' && !(item instanceof File)) {
				if (item instanceof Array) {
					item.forEach((f) => formData.append(ele, f || ''));
				} else {
					formData.append(ele, JSON.stringify(item));
				}
			} else {
				formData.append(ele, item);
			}
		}
	});

	return request<any>('/api/admin/v3/roles/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询角色列表 分页查询角色列表 POST /v3/roles/page */
export async function pageRole(body: API.RolePageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/api/admin/v3/roles/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 修改角色权限 修改角色权限 PUT /v3/roles/authority */
export async function modifyAuthorityRole(body: API.RoleModifyAuthorityCmd, options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/roles/authority', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
