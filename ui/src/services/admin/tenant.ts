// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** 修改租户 修改租户 PUT /v3/tenants */
export async function modifyV3(body: API.TenantModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/tenants', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存租户 保存租户 POST /v3/tenants */
export async function saveV3(body: API.TenantSaveCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/tenants', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除租户 删除租户 DELETE /v3/tenants */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
	return request<any>('/v3/tenants', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看租户详情 查看租户详情 GET /v3/tenants/${param0} */
export async function getByIdV3(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdV31Params,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/v3/tenants/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出租户 导出租户 POST /v3/tenants/export */
export async function exportV3(body: API.TenantExportCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/tenants/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入租户 导入租户 POST /v3/tenants/import */
export async function importV3(body: {}, file?: File[], options?: { [key: string]: any }) {
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

	return request<any>('/v3/tenants/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询租户列表 分页查询租户列表 POST /v3/tenants/page */
export async function pageV3(body: API.TenantPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/v3/tenants/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
