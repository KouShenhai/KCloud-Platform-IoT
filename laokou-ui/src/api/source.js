import request from '@/utils/request'

// 查询数据源列表
export function list (query) {
  return request({
    url: '/admin/v1/sources/list',
    method: 'post',
    data: query
  })
}

// 查询数据源
export function findById (id) {
  return request({
    url: '/admin/v1/sources/' + id,
    method: 'get'
  })
}

// 新增数据源
export function create (data, token) {
  return request({
    url: '/admin/v1/sources',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Request-Id': token
    }
  })
}

// 修改数据源
export function modify (data) {
  return request({
    url: '/admin/v1/sources',
    method: 'put',
    data: data
  })
}

// 删除数据源
export function remove (ids) {
  return request({
    url: '/admin/v1/sources',
    method: 'delete',
    data: ids
  })
}

// 数据源下拉列表
export function findOptionList () {
  return request({
    url: '/admin/v1/sources/option-list',
    method: 'get'
  })
}
