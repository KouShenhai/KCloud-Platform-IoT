/* eslint-disable */
import {request} from '@umijs/max';

/** 修改OSS日志 修改OSS日志 PUT /v3/oss-logs */
export async function modifyV3(body: API.OssLogModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/oss-logs', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存OSS日志 保存OSS日志 POST /v3/oss-logs */
export async function saveV3(body: API.OssLogSaveCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/oss-logs', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除OSS日志 删除OSS日志 DELETE /v3/oss-logs */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
	return request<any>('/v3/oss-logs', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看OSS日志详情 查看OSS日志详情 GET /v3/oss-logs/${param0} */
export async function getByIdV3(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdV3Params,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/v3/oss-logs/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出OSS日志 导出OSS日志 POST /v3/oss-logs/export */
export async function exportV3(body: API.OssLogExportCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/oss-logs/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入OSS日志 导入OSS日志 POST /v3/oss-logs/import */
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

	return request<any>('/v3/oss-logs/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询OSS日志列表 分页查询OSS日志列表 POST /v3/oss-logs/page */
export async function pageV3(body: API.OssLogPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/v3/oss-logs/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
