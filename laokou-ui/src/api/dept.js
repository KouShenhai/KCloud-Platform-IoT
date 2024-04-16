import request from '@/utils/request'

// 查询部门列表
export function list (data) {
  return request({
    url: '/admin/v1/depts/list',
    method: 'post',
    data: data
  })
}

// 查询部门详细
export function findById (id) {
  return request({
    url: '/admin/v1/depts/' + id,
    method: 'get'
  })
}

// 新增部门
export function create (data, token) {
  return request({
    url: '/admin/v1/depts',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Request-Id': token
    }
  })
}

// 修改部门
export function modify (data) {
  return request({
    url: '/admin/v1/depts',
    method: 'put',
    data: data
  })
}

// 删除部门
export function remove (ids) {
  return request({
    url: '/admin/v1/depts',
    method: 'delete',
    data: ids
  })
}

// 根据角色ID查询部门IDS
export function findIds (roleId) {
  return request({
    url: '/admin/v1/depts/' + roleId + '/ids',
    method: 'get'
  })
}
