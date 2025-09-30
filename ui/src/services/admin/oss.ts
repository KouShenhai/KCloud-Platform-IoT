/* eslint-disable */
import {request} from '@umijs/max';

/** 修改OSS 修改OSS PUT /api/v1/oss */
export async function modifyOss(body: API.OssModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/apis/admin/v1/oss', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存OSS 保存OSS POST /api/v1/oss */
export async function saveOss(body: API.OssSaveCmd, options?: { [key: string]: any }) {
	return request<any>('/apis/admin/v1/oss', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除OSS 删除OSS DELETE /api/v1/oss */
export async function removeOss(body: number[], options?: { [key: string]: any }) {
	return request<any>('/apis/admin/v1/oss', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看OSS详情 查看OSS详情 GET /api/v1/oss/${param0} */
export async function getOssById(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdParams,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/apis/admin/v1/oss/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出OSS 导出OSS POST /api/v1/oss/export */
export async function exportOss(body: API.OssExportCmd, options?: { [key: string]: any }) {
	return request<any>('/apis/admin/v1/oss/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入OSS 导入OSS POST /api/v1/oss/import */
export async function importOss(body: {}, file?: File[], options?: { [key: string]: any }) {
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

	return request<any>('/apis/admin/v1/oss/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询OSS列表 分页查询OSS列表 POST /api/v1/oss/page */
export async function pageOss(body: API.OssPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/apis/admin/v1/oss/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
