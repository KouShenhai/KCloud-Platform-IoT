/* eslint-disable */
import {request} from '@umijs/max';

/** 获取密钥 获取密钥 GET /v3/secrets */
export async function getInfoSecret(options?: { [key: string]: any }) {
	return request<API.Result>('/api/auth/v3/secrets', {
		method: 'GET',
		...(options || {}),
	});
}
