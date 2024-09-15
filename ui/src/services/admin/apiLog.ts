// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** 修改Api日志 修改Api日志 PUT /v3/api-logs */
export async function modifyV3(body: API.ApiLogModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/api-logs', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存Api日志 保存Api日志 POST /v3/api-logs */
export async function saveV3(body: API.ApiLogSaveCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/api-logs', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除Api日志 删除Api日志 DELETE /v3/api-logs */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
	return request<any>('/v3/api-logs', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看Api日志详情 查看Api日志详情 GET /v3/api-logs/${param0} */
export async function getByIdV3(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdV314Params,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/v3/api-logs/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出Api日志 导出Api日志 POST /v3/api-logs/export */
export async function exportV3(body: API.ApiLogExportCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/api-logs/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入Api日志 导入Api日志 POST /v3/api-logs/import */
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

	return request<any>('/v3/api-logs/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询Api日志列表 分页查询Api日志列表 POST /v3/api-logs/page */
export async function pageV3(body: API.ApiLogPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/v3/api-logs/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
