// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 查看令牌 查看令牌 GET /v3/tokens */
export async function getTokenV3(options?: { [key: string]: any }) {
  return request<API.Result>('/api/auth/v3/tokens', {
    method: 'GET',
    ...(options || {}),
  });
}
