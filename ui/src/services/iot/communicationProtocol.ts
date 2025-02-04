/* eslint-disable */
import { request } from '@umijs/max';

/** 修改通讯协议 修改通讯协议 PUT /v3/communication-protocols */
export async function modifyV3(
  body: API.CommunicationProtocolModifyCmd,
  options?: { [key: string]: any },
) {
  return request<any>('/v3/communication-protocols', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 保存通讯协议 保存通讯协议 POST /v3/communication-protocols */
export async function saveV3(
  body: API.CommunicationProtocolSaveCmd,
  options?: { [key: string]: any },
) {
  return request<any>('/v3/communication-protocols', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除通讯协议 删除通讯协议 DELETE /v3/communication-protocols */
export async function removeV3(body: number[], options?: { [key: string]: any }) {
  return request<any>('/v3/communication-protocols', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 查看通讯协议详情 查看通讯协议详情 GET /v3/communication-protocols/${param0} */
export async function getByIdV3(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByIdV3Params,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Result>(`/v3/communication-protocols/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 导出通讯协议 导出通讯协议 POST /v3/communication-protocols/export */
export async function exportV3(
  body: API.CommunicationProtocolExportCmd,
  options?: { [key: string]: any },
) {
  return request<any>('/v3/communication-protocols/export', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 导入通讯协议 导入通讯协议 POST /v3/communication-protocols/import */
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

  return request<any>('/v3/communication-protocols/import', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  });
}

/** 分页查询通讯协议列表 分页查询通讯协议列表 POST /v3/communication-protocols/page */
export async function pageV3(
  body: API.CommunicationProtocolPageQry,
  options?: { [key: string]: any },
) {
  return request<API.Result>('/v3/communication-protocols/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
