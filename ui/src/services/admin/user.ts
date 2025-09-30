/* eslint-disable */
import {request} from '@umijs/max';

/** 修改用户 修改用户 PUT /api/v1/users */
export async function modifyUser(body: API.UserModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/apis/admin/v1/users', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存用户 保存用户 POST /api/v1/users */
export async function saveUser(body: API.UserSaveCmd, requestId: string, options?: { [key: string]: any }) {
	return request<any>('/apis/admin/v1/users', {
		method: 'POST',
		headers: {
			'request-id': requestId,
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除用户 删除用户 DELETE /api/v1/users */
export async function removeUser(body: number[], options?: { [key: string]: any }) {
	return request<any>('/apis/admin/v1/users', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看用户详情 查看用户详情 GET /api/v1/users/${param0} */
export async function getUserById(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdParams,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/apis/admin/v1/users/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出用户 导出用户 POST /api/v1/users/export */
export async function exportUser(body: API.UserExportCmd, options?: { [key: string]: any }) {
	return request<any>('/apis/admin/v1/users/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入用户 导入用户 POST /api/v1/users/import */
export async function importUser(body: {}, file?: File[], options?: { [key: string]: any }) {
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

	return request<any>('/apis/admin/v1/users/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询用户列表 分页查询用户列表 POST /api/v1/users/page */
export async function pageUser(body: API.UserPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/apis/admin/v1/users/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看个人信息 查看个人信息 GET /api/v1/users/profile */
export async function getUserProfile(options?: { [key: string]: any }) {
	return request<API.Result>('/apis/admin/v1/users/profile', {
		method: 'GET',
		...(options || {}),
	});
}

/** 重置密码 重置密码 PUT /api/v1/users/reset-pwd */
export async function resetUserPwd(body: API.ResetPwdCmd, options?: { [key: string]: any }) {
	return request<any>('/apis/admin/v1/users/reset-pwd', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 修改用户权限 修改用户权限 PUT /api/v1/users/authority */
export async function modifyUserAuthority(body: API.UserModifyAuthorityCmd, options?: { [key: string]: any }) {
	return request<any>('/apis/admin/v1/users/authority', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 上传用户头像 上传用户头像 POST /api/v1/users/upload */
export async function uploadUserAvatar(body: FormData, options?: { [key: string]: any }) {
	return request<API.Result>('/apis/admin/v1/users/upload', {
		method: 'POST',
		headers: {
			'Content-Type': 'multipart/form-data',
		},
		data: body,
		...(options || {}),
	});
}
