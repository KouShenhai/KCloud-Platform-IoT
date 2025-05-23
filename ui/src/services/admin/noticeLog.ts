/* eslint-disable */
import {request} from '@umijs/max';
import {ExportAllToExcel} from "@/utils/export";
import moment from "moment";

/** 修改通知日志 修改通知日志 PUT /v3/notice-logs */
export async function modifyNoticeLog(body: API.NoticeLogModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/notice-logs', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存通知日志 保存通知日志 POST /v3/notice-logs */
export async function saveNoticeLog(body: API.NoticeLogSaveCmd, options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/notice-logs', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除通知日志 删除通知日志 DELETE /v3/notice-logs */
export async function removeNoticeLog(body: number[], options?: { [key: string]: any }) {
	return request<any>('/api/admin/v3/notice-logs', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看通知日志详情 查看通知日志详情 GET /v3/notice-logs/${param0} */
export async function getByIdNoticeLog(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdParams,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/api/admin/v3/notice-logs/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出通知日志 导出通知日志 POST /v3/notice-logs/export */
export async function exportNoticeLog(body: API.NoticeLogExportCmd, options?: { [key: string]: any }) {
	return ExportAllToExcel("通知日志" + "_导出全部_" + moment(new Date()).format('YYYYMMDDHHmmss') + ".xlsx", '/api/admin/v3/notice-logs/export', 'POST', body, options)
}

/** 导入通知日志 导入通知日志 POST /v3/notice-logs/import */
export async function importNoticeLog(body: {}, file?: File[], options?: { [key: string]: any }) {
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

	return request<any>('/api/admin/v3/notice-logs/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询通知日志列表 分页查询通知日志列表 POST /v3/notice-logs/page */
export async function pageNoticeLog(body: API.NoticeLogPageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/api/admin/v3/notice-logs/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
