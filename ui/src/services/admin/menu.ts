/* eslint-disable */
import {request} from '@umijs/max';

/** 修改菜单 修改菜单 PUT /v3/menus */
export async function modifyV3(body: API.MenuModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/menus', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存菜单 保存菜单 POST /v3/menus */
export async function saveV3(body: API.MenuSaveCmd, requestId: string, options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/menus', {
		method: 'POST',
		headers: {
			'request-id': requestId,
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除菜单 删除菜单 DELETE /v3/menus */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/menus', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看菜单详情 查看菜单详情 GET /v3/menus/${param0} */
export async function getByIdV3(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdV3Params,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/api/admin/v3/menus/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出菜单 导出菜单 POST /v3/menus/export */
export async function exportV3(body: API.MenuExportCmd, options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/menus/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入菜单 导入菜单 POST /v3/menus/import */
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

	return request<any>('/api/admin/v3/menus/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询菜单列表 分页查询菜单列表 POST /v3/menus/page */
export async function pageV3(body: API.MenuPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/api/admin/v3/menus/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

export async function treeListV3(body: any,options?: { [key: string]: any }) {
	return request<API.Result>('/api/admin/v3/menus/tree-list', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

export async function userTreeListV3(body: any,options?: { [key: string]: any }) {
	return request<API.Result>('/api/admin/v3/menus/user-tree-list', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
