// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** 退出登录 清除令牌 DELETE /v3/logouts */
export async function logoutV3(
	body: API.LogoutParam,
	options?: { [key: string]: any },
) {
	return request<any>('/api/auth/v3/logouts', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
