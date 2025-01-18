// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 修改设备 修改设备 PUT /v3/devices */
export async function modifyV3(body: API.DeviceModifyCmd, options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/devices', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 保存设备 保存设备 POST /v3/devices */
export async function saveV3(body: API.DeviceSaveCmd, options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/devices', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除设备 删除设备 DELETE /v3/devices */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/devices', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 查看设备详情 查看设备详情 GET /v3/devices/${param0} */
export async function getByIdV3(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByIdV3Params,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Result>(`/v3/devices/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 导出设备 导出设备 POST /v3/devices/export */
export async function exportV3(body: API.DeviceExportCmd, options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/devices/export', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 导入设备 导入设备 POST /v3/devices/import */
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

  return request<any>('/api/iot/v3/devices/import', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  });
}

/** 分页查询设备列表 分页查询设备列表 POST /v3/devices/page */
export async function pageV3(body: API.DevicePageQry, options?: { [key: string]: any }) {
  return request<API.Result>('/api/iot/v3/devices/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
