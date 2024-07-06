// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 验证码 根据UUID获取验证码 GET /v3/captchas/${param0} */
export async function getByUuidV3(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByUuidV3Params,
  options?: { [key: string]: any },
) {
  const { uuid: param0, ...queryParams } = params;
  return request<API.Result>(`/v3/captchas/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}
