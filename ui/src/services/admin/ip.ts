/* eslint-disable */
import {request} from '@umijs/max';

/** 修改IP 修改IP PUT /v3/ips */
export async function modifyV3(body: API.IpModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/ips', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存IP 保存IP POST /v3/ips */
export async function saveV3(body: API.IpSaveCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/ips', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除IP 删除IP DELETE /v3/ips */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
	return request<any>('/v3/ips', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看IP详情 查看IP详情 GET /v3/ips/${param0} */
export async function getByIdV3(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdV3Params,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/v3/ips/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出IP 导出IP POST /v3/ips/export */
export async function exportV3(body: API.IpExportCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/ips/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入IP 导入IP POST /v3/ips/import */
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

	return request<any>('/v3/ips/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询IP列表 分页查询IP列表 POST /v3/ips/page */
export async function pageV3(body: API.IpPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/v3/ips/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
