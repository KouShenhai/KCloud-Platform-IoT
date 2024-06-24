import { request } from '@umijs/max';

// 查询字典数据列表
export async function getDictDataList(
  params?: API.System.DictDataListParams,
  options?: { [key: string]: any },
) {
  return request<API.System.DictDataPageResult>('/api/system/dict/data/list', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
    ...(options || {}),
  });
}

// 查询字典数据详细
export function getDictData(dictCode: number, options?: { [key: string]: any }) {
  return request<API.System.DictDataInfoResult>(`/api/system/dict/data/${dictCode}`, {
    method: 'GET',
    ...(options || {}),
  });
}

// 新增字典数据
export async function addDictData(params: API.System.DictData, options?: { [key: string]: any }) {
  return request<API.Result>('/api/system/dict/data', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: params,
    ...(options || {}),
  });
}

// 修改字典数据
export async function updateDictData(params: API.System.DictData, options?: { [key: string]: any }) {
  return request<API.Result>('/api/system/dict/data', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: params,
    ...(options || {}),
  });
}

// 删除字典数据
export async function removeDictData(ids: string, options?: { [key: string]: any }) {
  return request<API.Result>(`/api/system/dict/data/${ids}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

// 导出字典数据
export function exportDictData(
  params?: API.System.DictDataListParams,
  options?: { [key: string]: any },
) {
  return request<API.Result>(`/api/system/dict/data/export`, {
    method: 'GET',
    params,
    ...(options || {}),
  });
}
