/* eslint-disable */
import {request} from '@umijs/max';

/** 修改字典 修改字典 PUT /api/v1/dicts */
export async function modifyDict(body: API.DictModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/api/v1/dicts', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存字典 保存字典 POST /api/v1/dicts */
export async function saveDict(body: API.DictSaveCmd, options?: { [key: string]: any }) {
	return request<any>('/api/v1/dicts', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除字典 删除字典 DELETE /api/v1/dicts */
export async function removeDict(body: number[], options?: { [key: string]: any }) {
	return request<any>('/api/v1/dicts', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看字典详情 查看字典详情 GET /api/v1/dicts/${param0} */
export async function getDictById(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdParams,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/api/v1/dicts/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出字典 导出字典 POST /api/v1/dicts/export */
export async function exportDict(body: API.DictExportCmd, options?: { [key: string]: any }) {
	return request<any>('/api/v1/dicts/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入字典 导入字典 POST /api/v1/dicts/import */
export async function importDict(body: {}, file?: File[], options?: { [key: string]: any }) {
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

	return request<any>('/api/v1/dicts/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询字典列表 分页查询字典列表 POST /api/v1/dicts/page */
export async function pageDict(body: API.DictPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/api/v1/dicts/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
