/* eslint-disable */
import { request } from '@umijs/max';

/** 修改产品类别 修改产品类别 PUT /v3/product-categorys */
export async function modifyV3(
  body: API.ProductCategoryModifyCmd,
  options?: { [key: string]: any },
) {
  return request<any>('/api/iot/v3/product-categorys', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 保存产品类别 保存产品类别 POST /v3/product-categorys */
export async function saveV3(body: API.ProductCategorySaveCmd, options?: { [key: string]: any }) {
  return request<any>('/v3/product-categorys', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除产品类别 删除产品类别 DELETE /v3/product-categorys */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/product-categorys', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 查看产品类别详情 查看产品类别详情 GET /v3/product-categorys/${param0} */
export async function getByIdV3(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByIdV3Params,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Result>(`/api/iot/v3/product-categorys/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 导出产品类别 导出产品类别 POST /v3/product-categorys/export */
export async function exportV3(
  body: API.ProductCategoryExportCmd,
  options?: { [key: string]: any },
) {
  return request<any>('/api/iot/v3/product-categorys/export', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 导入产品类别 导入产品类别 POST /v3/product-categorys/import */
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

  return request<any>('/api/iot/v3/product-categorys/import', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  });
}

/** 分页查询产品类别列表 分页查询产品类别列表 POST /v3/product-categorys/page */
export async function pageV3(body: API.ProductCategoryPageQry, options?: { [key: string]: any }) {
  return request<API.Result>('/api/iot/v3/product-categorys/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
