/* eslint-disable */
import { request } from '@umijs/max';

/** 修改网络连接 PUT /api/v1/sessions */
export async function modifySession(
	body: API.SessionModifyCmd,
	options?: { [key: string]: any },
) {
	return request<any>('/api-proxy/iot/api/v1/sessions', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** Save session POST /api/v1/sessions */
export async function saveCSession(
	body: API.SessionSaveCmd,
	requestId: string,
	options?: { [key: string]: any },
) {
	return request<any>('/api-proxy/iot/api/v1/sessions', {
		method: 'POST',
		headers: {
			'request-id': requestId,
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** Remove session DELETE /api/v1/sessions */
export async function removeSession(
	body: number[],
	options?: { [key: string]: any },
) {
	return request<any>('/api-proxy/iot/api/v1/sessions', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** Get session detail GET /api/v1/sessions/${param0} */
export async function getSessionById(
	params: API.SessionGetByIdParams,
	options?: { [key: string]: any },
) {
	const { id: param0, ...queryParams } = params;
	return request<API.Result>(`/api-proxy/iot/api/v1/sessions/${param0}`, {
		method: 'GET',
		params: { ...queryParams },
		...(options || {}),
	});
}

/** Page sessions POST /api/v1/sessions/page */
export async function pageSession(
	body: API.SessionPageQry,
	options?: { [key: string]: any },
) {
	return request<API.Result>('/api-proxy/iot/api/v1/sessions/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
