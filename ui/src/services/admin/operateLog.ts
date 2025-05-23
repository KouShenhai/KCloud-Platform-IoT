/* eslint-disable */
import {request} from '@umijs/max';
import {ExportAllToExcel} from "@/utils/export";
import moment from "moment";

/** 修改操作日志 修改操作日志 PUT /v3/operate-logs */
export async function modifyOperateLog(body: API.OperateLogModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/operate-logs', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存操作日志 保存操作日志 POST /v3/operate-logs */
export async function saveOperateLog(body: API.OperateLogSaveCmd, options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/operate-logs', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除操作日志 删除操作日志 DELETE /v3/operate-logs */
export async function removeOperateLog(body: number[], options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/operate-logs', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看操作日志详情 查看操作日志详情 GET /v3/operate-logs/${param0} */
export async function getByIdOperateLog(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdParams,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/api/admin/v3/operate-logs/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出操作日志 导出操作日志 POST /v3/operate-logs/export */
export async function exportOperateLog(body: API.OperateLogExportCmd, options?: { [key: string]: any }) {
	return ExportAllToExcel("操作日志" + "_导出全部_" + moment(new Date()).format('YYYYMMDDHHmmss') + ".xlsx", '/api/admin/v3/operate-logs/export', 'POST', body, options)
}

/** 导入操作日志 导入操作日志 POST /v3/operate-logs/import */
export async function importOperateLog(body: {}, file?: File[], options?: { [key: string]: any }) {
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

	return request<any>('/api/admin/v3/operate-logs/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询操作日志列表 分页查询操作日志列表 POST /v3/operate-logs/page */
export async function pageOperateLog(body: API.OperateLogPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/api/admin/v3/operate-logs/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
