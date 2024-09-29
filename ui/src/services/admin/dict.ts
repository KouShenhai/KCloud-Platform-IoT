// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** 修改字典 修改字典 PUT /v3/dicts */
export async function modifyV3(body: API.DictModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/dicts', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存字典 保存字典 POST /v3/dicts */
export async function saveV3(body: API.DictSaveCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/dicts', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除字典 删除字典 DELETE /v3/dicts */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
	return request<any>('/v3/dicts', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看字典详情 查看字典详情 GET /v3/dicts/${param0} */
export async function getByIdV3(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdV3Params,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/v3/dicts/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出字典 导出字典 POST /v3/dicts/export */
export async function exportV3(body: API.DictExportCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/dicts/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入字典 导入字典 POST /v3/dicts/import */
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

	return request<any>('/v3/dicts/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询字典列表 分页查询字典列表 POST /v3/dicts/page */
export async function pageV3(body: API.DictPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/v3/dicts/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
