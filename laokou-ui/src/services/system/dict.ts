import { request } from '@umijs/max';
import { DictValueEnumObj } from '@/components/DictTag';
import { HttpResult } from '@/enums/httpEnum';


// 查询字典类型列表
export async function getDictTypeList(params?: API.DictTypeListParams) {
  return request(`/api/system/dict/type/list`, {
    params: {
      ...params,
    },
    method: 'GET',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  });
}

// 查询字典类型详细
export function getDictType(dictId: string) {
  return request(`/api/system/dict/type/${dictId}`, {
    method: 'GET',
  });
}

// 查询字典数据详细
export async function getDictValueEnum(dictType: string, isDigital?: boolean): Promise<DictValueEnumObj> {
  const resp = await request<API.System.DictTypeResult>(`/api/system/dict/data/type/${dictType}`, {
    method: 'GET',
  });
  if(resp.code === HttpResult.SUCCESS) {
    const opts: DictValueEnumObj = {};
    resp.data.forEach((item: any) => {
      opts[item.dictValue] = {
        text: item.dictLabel,
        label: item.dictLabel,
        value: isDigital ? Number(item.dictValue) : item.dictValue,
        key: item.dictCode,
        listClass: item.listClass,
        status: item.listClass };
    });
    return opts;
  } else {
    return {};
  }
}

export async function getDictSelectOption(dictType: string, isDigital?: boolean) {
  const resp = await request<API.System.DictTypeResult>(`/api/system/dict/data/type/${dictType}`, {
    method: 'GET',
  });
  if (resp.code === 200) {
    const options: DictValueEnumObj[] = resp.data.map((item) => {
      return {
        text: item.dictLabel,
        label: item.dictLabel,
        value: isDigital ? Number(item.dictValue) : item.dictValue,
        key: item.dictCode,
        listClass: item.listClass,
        status: item.listClass
      };
    });
    return options;
  }
  return [];
};

// 新增字典类型
export async function addDictType(params: API.System.DictType) {
  return request<API.Result>('/api/system/dict/type', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: params
  });
}

// 修改字典类型
export async function updateDictType(params: API.System.DictType) {
  return request<API.Result>('/api/system/dict/type', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: params
  });
}

// 删除字典类型
export async function removeDictType(ids: string) {
  return request<API.Result>(`/api/system/dict/type/${ids}`, {
    method: 'DELETE'
  });
}

// 导出字典类型
export function exportDictType(params?: API.System.DictTypeListParams) {
  return request<API.Result>('/api/system/dict/type/export', {
    method: 'GET',
    params
  });
}

// 获取字典选择框列表
export async function getDictTypeOptionSelect(params?: API.DictTypeListParams) {
  return request('/api/system/dict/type/optionselect', {
    params: {
      ...params,
    },
    method: 'GET',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  });
}
