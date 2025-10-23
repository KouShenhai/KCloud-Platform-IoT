import {request} from '@umijs/max';

async function getToken(
	params: any,
	options?: { [key: string]: any },
) {
	return request<API.Result>(`/apis/auth/api/v1/oauth2/token`, {
		method: 'POST',
		data: params,
		// 设置序列化请求函数
		transformRequest: (data = {}) =>
			Object.entries(data)
				.map((ent) => ent.join('='))
				.join('&'),
		headers: {
			Authorization: 'Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=',
			'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
			'Skip-Token' : true
		},
		...(options || {}),
	});
}

/** OAuth2 认证授权 POST /oauth2/token */
export async function login(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.OAuth2Param,
	options?: { [key: string]: any },
) {
	return getToken(params, options)
}

/** OAuth2 认证授权 POST /oauth2/token */
export async function refresh(
	params: API.RefreshTokenParam,
	options?: { [key: string]: any }
) {
	return getToken(params, options)
}

/** 清除令牌 清除令牌 DELETE /api/v1/logouts */
export async function logout(
	body: API.LogoutParam,
	options?: { [key: string]: any },
) {
	return request<any>('/apis/auth/api/v1/tokens', {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
