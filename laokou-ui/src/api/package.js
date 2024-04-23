import request from '@/utils/request'

// 查询套擦
export function list (query) {
  return request({
    url: '/admin/v1/packages/list',
    method: 'post',
    data: query
  })
}

// 查询套餐详情
export function findById (id) {
  return request({
    url: '/admin/v1/packages/' + id,
    method: 'get'
  })
}

// 新增套餐
export function create (data, token) {
  return request({
    url: '/admin/v1/packages',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Request-Id': token
    }
  })
}

// 修改套餐
export function modify (data) {
  return request({
    url: '/admin/v1/packages',
    method: 'put',
    data: data
  })
}

// 删除套餐
export function remove (ids) {
  return request({
    url: '/admin/v1/packages',
    method: 'delete',
    data: ids
  })
}

// 套餐下拉列表
export function findOptionList () {
  return request({
    url: '/admin/v1/packages/option-list',
    method: 'get'
  })
}
