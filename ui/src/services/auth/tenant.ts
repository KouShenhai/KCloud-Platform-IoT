// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** 根据域名查看ID 根据域名查看ID GET /v3/tenants/id */
export async function getTenantIdByDomainNameV3(options?: { [key: string]: any }) {
	return request<API.Result>('/api/auth/v3/tenants/id', {
		method: 'GET',
		...(options || {}),
	});
}

/** 查询租户下拉选择项列表 查询租户下拉选择项列表 GET /v3/tenants/options */
export async function listTenantOptionV3(options?: { [key: string]: any }) {
	return request<API.Result>('/api/auth/v3/tenants/options', {
		method: 'GET',
		...(options || {}),
	});
}
