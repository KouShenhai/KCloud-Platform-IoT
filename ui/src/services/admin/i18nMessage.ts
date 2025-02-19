/* eslint-disable */
import {request} from '@umijs/max';

/** 修改国际化消息 修改国际化消息 PUT /v3/i18n-messages */
export async function modifyV3(body: API.I18nMessageModifyCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/i18n-messages', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 保存国际化消息 保存国际化消息 POST /v3/i18n-messages */
export async function saveV3(body: API.I18nMessageSaveCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/i18n-messages', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 删除国际化消息 删除国际化消息 DELETE /v3/i18n-messages */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
	return request<any>('/v3/i18n-messages', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 查看国际化消息详情 查看国际化消息详情 GET /v3/i18n-messages/${param0} */
export async function getByIdV3(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.getByIdV3Params,
	options?: { [key: string]: any },
) {
	const {id: param0, ...queryParams} = params;
	return request<API.Result>(`/v3/i18n-messages/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

/** 导出国际化消息 导出国际化消息 POST /v3/i18n-messages/export */
export async function exportV3(body: API.I18nMessageExportCmd, options?: { [key: string]: any }) {
	return request<any>('/v3/i18n-messages/export', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** 导入国际化消息 导入国际化消息 POST /v3/i18n-messages/import */
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

	return request<any>('/v3/i18n-messages/import', {
		method: 'POST',
		data: formData,
		requestType: 'form',
		...(options || {}),
	});
}

/** 分页查询国际化消息列表 分页查询国际化消息列表 POST /v3/i18n-messages/page */
export async function pageV3(body: API.I18nMessagePageQry, options?: { [key: string]: any }) {
	return request<API.Result>('/v3/i18n-messages/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
