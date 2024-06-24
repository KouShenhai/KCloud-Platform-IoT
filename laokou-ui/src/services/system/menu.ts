import { request } from '@umijs/max'; 

// 查询菜单权限列表
export async function getMenuList(params?: API.System.MenuListParams, options?: { [key: string]: any }) {
  return request<API.System.MenuPageResult>('/api/system/menu/list', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params,
    ...(options || {}),
  });
}

// 查询菜单权限详细
export function getMenu(menuId: number, options?: { [key: string]: any }) {
  return request<API.System.MenuInfoResult>(`/api/system/menu/${menuId}`, {
    method: 'GET',
    ...(options || {})
  });
}

// 新增菜单权限
export async function addMenu(params: API.System.Menu, options?: { [key: string]: any }) {
  return request<API.Result>('/api/system/menu', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: params,
    ...(options || {})
  });
}

// 修改菜单权限
export async function updateMenu(params: API.System.Menu, options?: { [key: string]: any }) {
  return request<API.Result>('/api/system/menu', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: params,
    ...(options || {})
  });
}

// 删除菜单权限
export async function removeMenu(ids: string, options?: { [key: string]: any }) {
  return request<API.Result>(`/api/system/menu/${ids}`, {
    method: 'DELETE',
    ...(options || {})
  });
}

// 导出菜单权限
export function exportMenu(params?: API.System.MenuListParams, options?: { [key: string]: any }) { 
  return request<API.Result>(`/api/system/menu/export`, {
    method: 'GET',
    params,
    ...(options || {})
  });
}

// 查询菜单权限详细
export function getMenuTree() {
  return request('/api/system/menu/treeselect', {
    method: 'GET',
  });
}
