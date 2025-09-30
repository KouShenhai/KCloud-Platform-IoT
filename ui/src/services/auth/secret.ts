/* eslint-disable */
import {request} from '@umijs/max';

/** 获取密钥 获取密钥 GET /api/v1/secrets */
export async function getSecretInfo(options?: { [key: string]: any }) {
	return request<API.Result>('/apis/auth/v1/secrets', {
		method: 'GET',
		...(options || {}),
	});
}
