/* eslint-disable */
import { request } from '@umijs/max';

/** 修改网络连接 PUT /api/v1/connections */
export async function modifyConnection(
	body: API.ConnectionModifyCmd,
	options?: { [key: string]: any },
) {
	return request<any>('/api-proxy/network/api/v1/connections', {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** Save network connection POST /api/v1/connections */
export async function saveConnection(
	body: API.ConnectionSaveCmd,
	requestId: string,
	options?: { [key: string]: any },
) {
	return request<any>('/api-proxy/network/api/v1/connections', {
		method: 'POST',
		headers: {
			'request-id': requestId,
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** Remove network connection DELETE /api/v1/connections */
export async function removeConnection(
	body: number[],
	options?: { [key: string]: any },
) {
	return request<any>('/api-proxy/network/api/v1/connections', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}

/** Get network connection detail GET /api/v1/connections/${param0} */
export async function getConnectionById(
	params: API.ConnectionGetByIdParams,
	options?: { [key: string]: any },
) {
	const { id: param0, ...queryParams } = params;
	return request<API.Result>(`/api-proxy/network/api/v1/connections/${param0}`, {
		method: 'GET',
		params: { ...queryParams },
		...(options || {}),
	});
}

/** Page network connections POST /api/v1/connections/page */
export async function pageConnection(
	body: API.ConnectionPageQry,
	options?: { [key: string]: any },
) {
	return request<API.Result>('/api-proxy/network/api/v1/connections/page', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
