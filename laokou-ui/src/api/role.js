import request from '@/utils/request'

// 查询角色列表-分页
export function list (query) {
  return request({
    url: '/admin/v1/roles/list',
    method: 'post',
    data: query
  })
}

// 查看角色
export function findById (id) {
  return request({
    url: '/admin/v1/roles/' + id,
    method: 'get'
  })
}

// 查询角色列表
export function findOptionList () {
  return request({
    url: '/admin/v1/roles/option-list',
    method: 'get'
  })
}

// 新增角色
export function create (data, token) {
  return request({
    url: '/admin/v1/roles',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Request-Id': token
    }
  })
}

// 修改角色
export function modify (data) {
  return request({
    url: '/admin/v1/roles',
    method: 'put',
    data: data
  })
}

// 删除角色
export function remove (ids) {
  return request({
    url: '/admin/v1/roles',
    method: 'delete',
    data: ids
  })
}
