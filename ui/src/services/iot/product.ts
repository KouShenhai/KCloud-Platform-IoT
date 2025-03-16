/* eslint-disable */
import { request } from '@umijs/max';

/** 修改产品 修改产品 PUT /v3/products */
export async function modifyV3(body: API.ProductModifyCmd, options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/products', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 保存产品 保存产品 POST /v3/products */
export async function saveV3(body: API.ProductSaveCmd, options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/products', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除产品 删除产品 DELETE /v3/products */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/products', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 查看产品详情 查看产品详情 GET /v3/products/${param0} */
export async function getByIdV3(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByIdV3Params,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Result>(`/api/iot/v3/products/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 导出产品 导出产品 POST /v3/products/export */
export async function exportV3(body: API.ProductExportCmd, options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/products/export', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 导入产品 导入产品 POST /v3/products/import */
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

  return request<any>('/api/iot/v3/products/import', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  });
}

/** 分页查询产品列表 分页查询产品列表 POST /v3/products/page */
export async function pageV3(body: API.ProductPageQry, options?: { [key: string]: any }) {
  return request<API.Result>('/api/iot/v3/products/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
