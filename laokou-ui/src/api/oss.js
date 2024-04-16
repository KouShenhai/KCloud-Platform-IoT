import request from '@/utils/request'

// OSS上传文件
export function upload (data) {
  return request({
    url: '/admin/v1/oss/upload',
    method: 'post',
    data: data
  })
}

// 查询OSS列表
export function list (query) {
  return request({
    url: '/admin/v1/oss/list',
    method: 'post',
    data: query
  })
}

// 根据ID查看OSS
export function findById (id) {
  return request({
    url: '/admin/v1/oss/' + id,
    method: 'get'
  })
}

// 新增OSS
export function create (data, token) {
  return request({
    url: '/admin/v1/oss',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Request-Id': token
    }
  })
}

// 修改OSS
export function modify (data) {
  return request({
    url: '/admin/v1/oss',
    method: 'put',
    data: data
  })
}

// 删除OSS
export function remove (ids) {
  return request({
    url: '/admin/v1/oss',
    method: 'delete',
    data: ids
  })
}
