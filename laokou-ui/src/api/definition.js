import request from '@/utils/request'

// 分页
export function list (query) {
  return request({
    url: '/flowable/v1/definitions/list',
    method: 'post',
    data: query
  })
}

// 新增
export function create (data, token) {
  return request({
    url: '/flowable/v1/definitions',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Request-Id': token
    }
  })
}

// 挂起
export function suspend (definitionId) {
  return request({
    url: '/flowable/v1/definitions/' + definitionId + '/suspend',
    method: 'put'
  })
}

// 激活
export function activate (definitionId) {
  return request({
    url: '/flowable/v1/definitions/' + definitionId + '/activate',
    method: 'put'
  })
}

// 删除
export function remove (deploymentId) {
  return request({
    url: '/flowable/v1/definitions/' + deploymentId,
    method: 'delete'
  })
}

export function findDiagram (id) {
  return request({
    url: '/flowable/v1/definitions/' + id + '/diagram',
    method: 'get'
  })
}

export function downloadTemplate () {
  return request({
    url: '/admin/v1/definitions/download-template',
    method: 'get',
    responseType: 'blob'
  })
}
