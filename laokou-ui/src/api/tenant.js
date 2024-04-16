import request from '@/utils/request'
// 查询租户列表
export function list (query) {
  return request({
    url: '/admin/v1/tenants/list',
    method: 'post',
    data: query
  })
}

// 查询租户详细
export function findById (id) {
  return request({
    url: '/admin/v1/tenants/' + id,
    method: 'get'
  })
}

// 新增租户
export function create (data, token) {
  return request({
    url: '/admin/v1/tenants',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Request-Id': token
    }
  })
}

// 修改租户
export function modify (data) {
  return request({
    url: '/admin/v1/tenants',
    method: 'put',
    data: data
  })
}

// 删除租户
export function remove (ids) {
  return request({
    url: '/admin/v1/tenants',
    method: 'delete',
    data: ids
  })
}

// 解析域名查看ID
export function findIdByDomainName () {
  return request({
    url: '/admin/v1/tenants/id',
    method: 'get'
  })
}

export function downloadDatasource (id) {
  return request({
    url: '/admin/v1/tenants/' + id + '/download-datasource',
    method: 'get',
    responseType: 'blob'
  })
}
