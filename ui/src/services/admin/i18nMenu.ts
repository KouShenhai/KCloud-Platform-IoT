/* eslint-disable */
import { request } from '@umijs/max';

/** 修改国际化菜单 修改国际化菜单 PUT /api/v1/i18n-menus */
export async function modifyI18nMenu(
	body: API.I18nMenuModifyCmd,
	options?: { [key: string]: any },
) {
	return request<any>('/api-proxy/admin/api/v1/i18n-menus', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存国际化菜单 保存国际化菜单 POST /api/v1/i18n-menus */
export async function saveI18nMenu(
	body: API.I18nMenuSaveCmd,
	options?: { [key: string]: any },
) {
	return request<any>('/api-proxy/admin/api/v1/i18n-menus', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除国际化菜单 删除国际化菜单 DELETE /api/v1/i18n-menus */
export async function removeI18nMenu(
	body: number[],
	options?: { [key: string]: any },
) {
	return request<any>('/api-proxy/admin/api/v1/i18n-menus', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看国际化菜单详情 查看国际化菜单详情 GET /api/v1/i18n-menus/${param0} */
export async function getI18nMenuById(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdParams,
	options?: { [key: string]: any },
) {
	const { id: param0, ...queryParams } = params;
	return request<API.Result>(`/api-proxy/admin/api/v1/i18n-menus/${param0}`, {
		method: 'GET',
		params: { ...queryParams },
		...(options || {}),
	});
}

/** 导出国际化菜单 导出国际化菜单 POST /api/v1/i18n-menus/export */
export async function exportI18nMenu(
	body: API.I18nMenuExportCmd,
	options?: { [key: string]: any },
) {
	return request<any>('/api-proxy/admin/api/v1/i18n-menus/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入国际化菜单 导入国际化菜单 POST /api/v1/i18n-menus/import */
export async function importI18nMenu(
	body: {},
	file?: File[],
	options?: { [key: string]: any },
) {
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

	return request<any>('/api-proxy/admin/api/v1/i18n-menus/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询国际化菜单列表 分页查询国际化菜单列表 POST /api/v1/i18n-menus/page */
export async function pageI18nMenu(
	body: API.I18nMenuPageQry,
	options?: { [key: string]: any },
) {
	return request<API.Result>('/api-proxy/admin/api/v1/i18n-menus/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
