// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 安全配置 获取密钥 GET /v3/secrets */
export async function getInfoV3(options?: { [key: string]: any }) {
  return request<API.Result>('/v3/secrets', {
    method: 'GET',
    ...(options || {}),
  });
}
