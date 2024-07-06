// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 租户管理 根据域名查看ID GET /v3/tenants/id */
export async function getIdByDomainNameV3(options?: { [key: string]: any }) {
  return request<API.Result>('/v3/tenants/id', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 租户管理 查询租户下拉选择项列表 GET /v3/tenants/options */
export async function listOptionV3(options?: { [key: string]: any }) {
  return request<API.Result>('/v3/tenants/options', {
    method: 'GET',
    ...(options || {}),
  });
}
