/* eslint-disable */
import {request} from '@umijs/max';

/** 修改字典项 修改字典项 PUT /v3/dict-items */
export async function modifyV3(body: API.DictItemModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/dict-items', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存字典项 保存字典项 POST /v3/dict-items */
export async function saveV3(body: API.DictItemSaveCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/dict-items', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除字典项 删除字典项 DELETE /v3/dict-items */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
	return request<any>('/v3/dict-items', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看字典项详情 查看字典项详情 GET /v3/dict-items/${param0} */
export async function getByIdV3(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdV3Params,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/v3/dict-items/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出字典项 导出字典项 POST /v3/dict-items/export */
export async function exportV3(body: API.DictItemExportCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/dict-items/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入字典项 导入字典项 POST /v3/dict-items/import */
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

	return request<any>('/v3/dict-items/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询字典项列表 分页查询字典项列表 POST /v3/dict-items/page */
export async function pageV3(body: API.DictItemPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/v3/dict-items/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
