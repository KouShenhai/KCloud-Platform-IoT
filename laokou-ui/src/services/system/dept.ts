import { request } from '@umijs/max'; 

// 查询部门列表
export async function getDeptList(params?: API.System.DeptListParams) {
  return request<API.System.DeptPageResult>('/api/system/dept/list', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params
  });
}

// 查询部门列表（排除节点）
export function getDeptListExcludeChild(deptId: number) {
  return request(`/api/system/dept/list/exclude/${deptId}`, {
    method: 'get',
  });
}

// 查询部门详细
export function getDept(deptId: number) {
  return request<API.System.DeptInfoResult>(`/api/system/dept/${deptId}`, {
    method: 'GET'
  });
}

// 新增部门
export async function addDept(params: API.System.Dept) {
  return request<API.Result>('/api/system/dept', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: params
  });
}

// 修改部门
export async function updateDept(params: API.System.Dept) {
  return request<API.Result>('/api/system/dept', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: params
  });
}

// 删除部门
export async function removeDept(ids: string) {
  return request<API.Result>(`/api/system/dept/${ids}`, {
    method: 'DELETE'
  });
}

// 导出部门
export function exportDept(params?: API.System.DeptListParams) { 
  return request<API.Result>(`/api/system/dept/export`, {
    method: 'GET',
    params
  });
}
