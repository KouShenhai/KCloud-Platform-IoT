/* eslint-disable */
import { request } from '@umijs/max';

/** 修改通讯协议 修改通讯协议 PUT /v3/communication-protocols */
export async function modifyCommunicationProtocol(
  body: API.CommunicationProtocolModifyCmd,
  options?: { [key: string]: any },
) {
  return request<any>('/api/iot/v3/communication-protocols', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 保存通讯协议 保存通讯协议 POST /v3/communication-protocols */
export async function saveCommunicationProtocol(
  body: API.CommunicationProtocolSaveCmd,
  options?: { [key: string]: any },
) {
  return request<any>('/api/iot/v3/communication-protocols', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除通讯协议 删除通讯协议 DELETE /v3/communication-protocols */
export async function removeCommunicationProtocol(body: number[], options?: { [key: string]: any }) {
  return request<any>('/api/iot/v3/communication-protocols', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 查看通讯协议详情 查看通讯协议详情 GET /v3/communication-protocols/${param0} */
export async function getByIdCommunicationProtocol(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByIdParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Result>(`/api/iot/v3/communication-protocols/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 导出通讯协议 导出通讯协议 POST /v3/communication-protocols/export */
export async function exportCommunicationProtocol(
  body: API.CommunicationProtocolExportCmd,
  options?: { [key: string]: any },
) {
  return request<any>('/api/iot/v3/communication-protocols/export', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 导入通讯协议 导入通讯协议 POST /v3/communication-protocols/import */
export async function importCommunicationProtocol(body: {}, file?: File[], options?: { [key: string]: any }) {
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

  return request<any>('/api/iot/v3/communication-protocols/import', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  });
}

/** 分页查询通讯协议列表 分页查询通讯协议列表 POST /v3/communication-protocols/page */
export async function pageCommunicationProtocol(
  body: API.CommunicationProtocolPageQry,
  options?: { [key: string]: any },
) {
  return request<API.Result>('/api/iot/v3/communication-protocols/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
