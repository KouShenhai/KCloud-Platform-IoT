// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** 安全配置 获取密钥 GET /v3/secrets */
export async function getSecretInfoV3(options?: { [key: string]: any }) {
	return request<API.Result>('/api/auth/v3/secrets', {
		method: 'GET',
		...(options || {}),
	});
}
