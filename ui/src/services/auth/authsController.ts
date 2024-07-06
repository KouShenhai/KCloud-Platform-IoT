import {request} from '@umijs/max';

/** OAuth2 认证授权 POST /oauth2/token */
export async function login(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.OAuth2Param,
	options?: { [key: string]: any },
) {
	return request<API.Result>(`/api/auth/oauth2/token`, {
		method: 'POST',
		data: params,
		// 设置序列化请求函数
		transformRequest: (data = {}) => Object.entries(data).map(ent => ent.join('=')).join('&'),
		headers: {
			'Authorization': 'Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=',
			'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
		},
		...(options || {}),
	});
}
