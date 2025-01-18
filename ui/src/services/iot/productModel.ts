// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 修改产品模型 修改产品模型 PUT /v3/product-models */
export async function modifyV3(body: API.ProductModelModifyCmd, options?: { [key: string]: any }) {
  return request<any>('/v3/product-models', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 保存产品模型 保存产品模型 POST /v3/product-models */
export async function saveV3(body: API.ProductModelSaveCmd, options?: { [key: string]: any }) {
  return request<any>('/v3/product-models', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除产品模型 删除产品模型 DELETE /v3/product-models */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
  return request<any>('/v3/product-models', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 查看产品模型详情 查看产品模型详情 GET /v3/product-models/${param0} */
export async function getByIdV3(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByIdV3Params,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Result>(`/v3/product-models/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 导出产品模型 导出产品模型 POST /v3/product-models/export */
export async function exportV3(body: API.ProductModelExportCmd, options?: { [key: string]: any }) {
  return request<any>('/v3/product-models/export', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 导入产品模型 导入产品模型 POST /v3/product-models/import */
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

  return request<any>('/v3/product-models/import', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  });
}

/** 分页查询产品模型列表 分页查询产品模型列表 POST /v3/product-models/page */
export async function pageV3(body: API.ProductModelPageQry, options?: { [key: string]: any }) {
  return request<API.Result>('/v3/product-models/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
