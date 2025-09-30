/* eslint-disable */
import { request } from '@umijs/max';

/** 修改设备 修改设备 PUT /api/v1/devices */
export async function modifyDevice(body: API.DeviceModifyCmd, options?: { [key: string]: any }) {
  return request<any>('/apis/iot/api/v1/devices', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 保存设备 保存设备 POST /api/v1/devices */
export async function saveDevice(body: API.DeviceSaveCmd, options?: { [key: string]: any }) {
  return request<any>('/apis/iot/api/v1/devices', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除设备 删除设备 DELETE /api/v1/devices */
export async function removeDevice(body: number[], options?: { [key: string]: any }) {
  return request<any>('/apis/iot/api/v1/devices', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 查看设备详情 查看设备详情 GET /api/v1/devices/${param0} */
export async function getDeviceById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByIdParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Result>(`/apis/iot/api/v1/devices/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 导出设备 导出设备 POST /api/v1/devices/export */
export async function exportDevice(body: API.DeviceExportCmd, options?: { [key: string]: any }) {
  return request<any>('/apis/iot/api/v1/devices/export', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 导入设备 导入设备 POST /api/v1/devices/import */
export async function importDevice(body: {}, file?: File[], options?: { [key: string]: any }) {
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

  return request<any>('/apis/iot/api/v1/devices/import', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  });
}

/** 分页查询设备列表 分页查询设备列表 POST /api/v1/devices/page */
export async function pageDevice(body: API.DevicePageQry, options?: { [key: string]: any }) {
  return request<API.Result>('/apis/iot/api/v1/devices/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
