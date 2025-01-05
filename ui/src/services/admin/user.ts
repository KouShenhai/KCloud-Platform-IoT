// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** 修改用户 修改用户 PUT /v3/users */
export async function modifyV3(body: API.UserModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/users', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存用户 保存用户 POST /v3/users */
export async function saveV3(body: API.UserSaveCmd, options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/users', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除用户 删除用户 DELETE /v3/users */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/users', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看用户详情 查看用户详情 GET /v3/users/${param0} */
export async function getByIdV3(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdV3Params,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/api/admin/v3/users/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出用户 导出用户 POST /v3/users/export */
export async function exportV3(body: API.UserExportCmd, options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/users/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入用户 导入用户 POST /v3/users/import */
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

	return request<any>('/api/admin/v3/users/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询用户列表 分页查询用户列表 POST /v3/users/page */
export async function pageV3(body: API.UserPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/api/admin/v3/users/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看个人信息 查看个人信息 GET /v3/users/profile */
export async function getProfileV3(options?: { [key: string]: any }) {
	return request<API.Result>('/api/admin/v3/users/profile', {
		method: 'GET',
		...(options || {}),
	});
}
