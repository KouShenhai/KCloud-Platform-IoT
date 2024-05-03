import request from '@/utils/request'

// 查询菜单列表
export function getRouters () {
  return request({
    url: '/admin/v1/menus/routers',
    method: 'get'
  })
}

// 查看菜单
export function findById (id) {
  return request({
    url: '/admin/v1/menus' + '/' + id,
    method: 'get'
  })
}

// 新增菜单
export function create (data, token) {
  return request({
    url: '/admin/v1/menus',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Request-Id': token
    }
  })
}

// 修改菜单
export function modify (data) {
  return request({
    url: '/admin/v1/menus',
    method: 'put',
    data: data
  })
}

// 删除菜单
export function remove (ids) {
  return request({
    url: '/admin/v1/menus',
    method: 'delete',
    data: ids
  })
}

// 根据角色ID查询菜单树IDS
export function findIds (roleId) {
  return request({
    url: '/admin/v1/menus/' + roleId + '/ids',
    method: 'get'
  })
}

// 租户菜单
export function findTenantMenuList (data) {
  return request({
    url: '/admin/v1/menus/tenant-menu-list',
    method: 'post',
    data: data
  })
}
