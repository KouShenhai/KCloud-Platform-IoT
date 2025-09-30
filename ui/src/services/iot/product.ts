/* eslint-disable */
import { request } from '@umijs/max';

/** 修改产品 修改产品 PUT /api/v1/products */
export async function modifyProduct(body: API.ProductModifyCmd, options?: { [key: string]: any }) {
  return request<any>('/apis/iot/v1/products', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 保存产品 保存产品 POST /api/v1/products */
export async function saveProduct(body: API.ProductSaveCmd, options?: { [key: string]: any }) {
  return request<any>('/apis/iot/v1/products', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除产品 删除产品 DELETE /api/v1/products */
export async function removeProduct(body: number[], options?: { [key: string]: any }) {
  return request<any>('/apis/iot/v1/products', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 查看产品详情 查看产品详情 GET /api/v1/products/${param0} */
export async function getProductById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByIdParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Result>(`/apis/iot/v1/products/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 导出产品 导出产品 POST /api/v1/products/export */
export async function exportProduct(body: API.ProductExportCmd, options?: { [key: string]: any }) {
  return request<any>('/apis/iot/v1/products/export', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 导入产品 导入产品 POST /api/v1/products/import */
export async function importProduct(body: {}, file?: File[], options?: { [key: string]: any }) {
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

  return request<any>('/apis/iot/v1/products/import', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  });
}

/** 分页查询产品列表 分页查询产品列表 POST /api/v1/products/page */
export async function pageProduct(body: API.ProductPageQry, options?: { [key: string]: any }) {
  return request<API.Result>('/apis/iot/v1/products/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
