// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** 修改数据源 修改数据源 PUT /v3/sources */
export async function modifyV3(body: API.SourceModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/sources', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存数据源 保存数据源 POST /v3/sources */
export async function saveV3(body: API.SourceSaveCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/sources', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除数据源 删除数据源 DELETE /v3/sources */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
	return request<any>('/v3/sources', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看数据源详情 查看数据源详情 GET /v3/sources/${param0} */
export async function getByIdV3(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdV3Params,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/v3/sources/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出数据源 导出数据源 POST /v3/sources/export */
export async function exportV3(body: API.SourceExportCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/sources/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入数据源 导入数据源 POST /v3/sources/import */
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

	return request<any>('/v3/sources/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询数据源列表 分页查询数据源列表 POST /v3/sources/page */
export async function pageV3(body: API.SourcePageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/v3/sources/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
