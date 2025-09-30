/* eslint-disable */
import {request} from '@umijs/max';

/** 修改租户 修改租户 PUT /api/v1/tenants */
export async function modifyTenant(body: API.TenantModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/api/v1/tenants', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存租户 保存租户 POST /api/v1/tenants */
export async function saveTenant(body: API.TenantSaveCmd, options?: { [key: string]: any }) {
	return request<any>('/api/v1/tenants', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除租户 删除租户 DELETE /api/v1/tenants */
export async function removeTenant(body: number[], options?: { [key: string]: any }) {
	return request<any>('/api/v1/tenants', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看租户详情 查看租户详情 GET /api/v1/tenants/${param0} */
export async function getTenantById(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdParams,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/api/v1/tenants/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出租户 导出租户 POST /api/v1/tenants/export */
export async function exportTenant(body: API.TenantExportCmd, options?: { [key: string]: any }) {
	return request<any>('/api/v1/tenants/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入租户 导入租户 POST /api/v1/tenants/import */
export async function importTenant(body: {}, file?: File[], options?: { [key: string]: any }) {
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

	return request<any>('/api/v1/tenants/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询租户列表 分页查询租户列表 POST /api/v1/tenants/page */
export async function pageTenant(body: API.TenantPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/api/v1/tenants/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
