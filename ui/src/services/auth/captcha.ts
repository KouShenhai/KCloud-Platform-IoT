// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** 根据UUID获取验证码 根据UUID获取验证码 GET /v3/captchas/${param0} */
export async function getCaptchaImageByUuidV3(
	// 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
	params: API.CaptchaParams,
	options?: { [key: string]: any },
) {
	const {uuid: param0, ...queryParams} = params;
	return request<API.Result>(`/api/auth/v3/captchas/${param0}`, {
		method: 'GET',
		params: {...queryParams},
		...(options || {}),
	});
}

export async function sendCaptchaV3(
	type: string,
	body: API.SendCaptchaCO,
	requestId: string,
	options?: { [key: string]: any },
) {
	return request<any>(`/api/auth/v3/captchas/send/${type}`, {
		method: 'POST',
		headers: {
			'request-id': requestId,
			'Content-Type': 'application/json'
		},
		data: body,
		...(options || {}),
	});
}
