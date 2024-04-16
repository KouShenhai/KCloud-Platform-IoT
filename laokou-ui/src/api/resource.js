import request from '@/utils/request'
// 查询资源列表
export function listResource (query) {
  return request({
    url: '/admin/v1/resource/list',
    method: 'post',
    data: query
  })
}

// 资源任务分页
export function listResourceTask (query) {
  return request({
    url: '/admin/v1/resource/task-list',
    method: 'post',
    data: query
  })
}

// 根据ID获取资源
export function getResourceById (id) {
  return request({
    url: '/admin/v1/resource/' + id,
    method: 'get'
  })
}

// 详情
export function getResourceDetailTask (id) {
  return request({
    url: '/admin/v1/resource/' + id + '/detail-task',
    method: 'get'
  })
}

// 审批
export function auditResourceTask (data, token) {
  return request({
    url: '/admin/v1/resource/audit-task',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Request-Id': token
    }
  })
}

// 资源委派任务
export function delegateResourceTask (data, token) {
  return request({
    url: '/admin/v1/resource/delegate-task',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Request-Id': token
    }
  })
}

// 资源处理任务
export function resolveResourceTask (data, token) {
  return request({
    url: '/admin/v1/resource/resolve-task',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Request-Id': token
    }
  })
}

// 资源转办任务
export function transferResourceTask (data, token) {
  return request({
    url: '/admin/v1/resource/transfer-task',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Request-Id': token
    }
  })
}

// 修改资源
export function updateResource (data, token) {
  return request({
    url: '/admin/v1/resource',
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Request-Id': token
    }
  })
}

// 同步资源
export function searchResource (query) {
  return request({
    url: '/admin/v1/resource/search',
    method: 'post',
    data: query
  })
}

// 同步资源
export function syncResourceIndex () {
  return request({
    url: '/admin/v1/resource/sync',
    method: 'post'
  })
}

// 新增资源
export function insertResource (data, token) {
  return request({
    url: '/admin/v1/resource',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Request-Id': token
    }
  })
}

// 删除资源
export function deleteResourceById (id) {
  return request({
    url: '/admin/v1/resource/' + id,
    method: 'delete'
  })
}

// 获取审批记录
export function getResourceAuditLogById (id) {
  return request({
    url: '/admin/v1/resource/' + id + '/audit-log',
    method: 'get'
  })
}

// 审批流程图
export function getResourceDiagram (instanceId) {
  return request({
    url: '/admin/v1/resource/' + instanceId + '/diagram',
    method: 'get'
  })
}

// 下载资源
export function downloadResource (id) {
  return request({
    url: '/admin/v1/resource/' + id + '/download',
    method: 'get',
    responseType: 'blob'
  })
}
