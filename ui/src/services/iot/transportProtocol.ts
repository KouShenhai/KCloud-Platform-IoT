/* eslint-disable */
import { request } from '@umijs/max';

/** 修改传输协议 修改传输协议 PUT /v3/transport-protocols */
export async function modifyTransportProtocol(
  body: API.TransportProtocolModifyCmd,
  options?: { [key: string]: any },
) {
  return request<any>('/api/iot/v3/transport-protocols', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 保存传输协议 保存传输协议 POST /v3/transport-protocols */
export async function saveTransportProtocol(body: API.TransportProtocolSaveCmd, options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/transport-protocols', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除传输协议 删除传输协议 DELETE /v3/transport-protocols */
export async function removeTransportProtocol(body: number[], options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/transport-protocols', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 查看传输协议详情 查看传输协议详情 GET /v3/transport-protocols/${param0} */
export async function getTransportProtocolById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByIdParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Result>(`/api/iot/v3/transport-protocols/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 导出传输协议 导出传输协议 POST /v3/transport-protocols/export */
export async function exportTransportProtocol(
  body: API.TransportProtocolExportCmd,
  options?: { [key: string]: any },
) {
  return request<any>('/api/iot/v3/transport-protocols/export', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 导入传输协议 导入传输协议 POST /v3/transport-protocols/import */
export async function importTransportProtocol(body: {}, file?: File[], options?: { [key: string]: any }) {
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

  return request<any>('/api/iot/v3/transport-protocols/import', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  });
}

/** 分页查询传输协议列表 分页查询传输协议列表 POST /v3/transport-protocols/page */
export async function pageTransportProtocol(body: API.TransportProtocolPageQry, options?: { [key: string]: any }) {
  return request<API.Result>('/api/iot/v3/transport-protocols/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
