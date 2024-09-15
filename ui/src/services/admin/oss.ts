// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** 修改OSS 修改OSS PUT /v3/oss */
export async function modifyV3(body: API.OssModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/oss', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存OSS 保存OSS POST /v3/oss */
export async function saveV3(body: API.OssSaveCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/oss', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除OSS 删除OSS DELETE /v3/oss */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
	return request<any>('/v3/oss', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看OSS详情 查看OSS详情 GET /v3/oss/${param0} */
export async function getByIdV3(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdV34Params,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/v3/oss/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出OSS 导出OSS POST /v3/oss/export */
export async function exportV3(body: API.OssExportCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/oss/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入OSS 导入OSS POST /v3/oss/import */
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

	return request<any>('/v3/oss/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询OSS列表 分页查询OSS列表 POST /v3/oss/page */
export async function pageV3(body: API.OssPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/v3/oss/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
