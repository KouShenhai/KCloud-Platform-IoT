// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 退出登录 清除令牌 DELETE /v3/logouts */
export async function removeTokenV3(
  body: API.LogoutCmd,
  options?: { [key: string]: any },
) {
  return request<any>('/v3/logouts', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
