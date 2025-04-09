/* eslint-disable */
import { request } from '@umijs/max';

/** 修改物模型 修改物模型 PUT /v3/thing-models */
export async function modifyV3(body: API.ThingModelModifyCmd, options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/thing-models', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 保存物模型 保存物模型 POST /v3/thing-models */
export async function saveV3(body: API.ThingModelSaveCmd, requestId: string, options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/thing-models', {
    method: 'POST',
    headers: {
		'request-id': requestId,
		'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除物模型 删除物模型 DELETE /v3/thing-models */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/thing-models', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 查看物模型详情 查看物模型详情 GET /v3/thing-models/${param0} */
export async function getByIdV3(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByIdV3Params,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Result>(`/api/iot/v3/thing-models/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 导出物模型 导出物模型 POST /v3/thing-models/export */
export async function exportV3(
  body: API.ThingThingModelExportCmd,
  options?: { [key: string]: any },
) {
  return request<any>('/api/iot/v3/thing-models/export', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 导入物模型 导入物模型 POST /v3/thing-models/import */
export async function importV3(body: {}, file?: File[], options?: { [key: string]: any }) {
  const formData = new FormData();

  if (file) {
    file.forEach((f) => formData.append('file', f || ''));
  }

  Object.keys(body).forEach((ele) => {
    const item = (body as any)[ele];

    if (item !== undefined && item !== null) {
      if (typeof item === 'object' && !(item instanceof File)) {
        if (item instanceof Array) {
          item.forEach((f) => formData.append(ele, f || ''));
        } else {
          formData.append(ele, JSON.stringify(item));
        }
      } else {
        formData.append(ele, item);
      }
    }
  });

  return request<any>('/api/iot/v3/thing-models/import', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  });
}

/** 分页查询物模型列表 分页查询物模型列表 POST /v3/thing-models/page */
export async function pageV3(body: API.ThingModelPageQry, options?: { [key: string]: any }) {
  return request<API.Result>('/api/iot/v3/thing-models/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
