/* eslint-disable */
import { request } from '@umijs/max';

/** 修改产品类别 修改产品类别 PUT /v3/product-categorys */
export async function modifyProductCategory(
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
export async function saveProductCategory(body: API.ProductCategorySaveCmd, requestId: string, options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/product-categorys', {
    method: 'POST',
    headers: {
		'request-id': requestId,
		'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除产品类别 删除产品类别 DELETE /v3/product-categorys */
export async function removeProductCategory(body: number[], options?: { [key: string]: any }) {
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
export async function getByIdProductCategory(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByIdParams,
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
export async function exportProductCategory(
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
export async function importProductCategory(body: {}, file?: File[], options?: { [key: string]: any }) {
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
export async function pageProductCategory(body: API.ProductCategoryPageQry, options?: { [key: string]: any }) {
  return request<API.Result>('/api/iot/v3/product-categorys/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

export async function listTreeProductCategory(body: any,options?: { [key: string]: any }) {
	return request<API.Result>('/api/iot/v3/product-categorys/list-tree', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	});
}
