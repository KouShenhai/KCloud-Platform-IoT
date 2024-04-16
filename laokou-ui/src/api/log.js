import request from '@/utils/request'

// 查询登录日志列表
export function findLoginList (query) {
  return request({
    url: '/admin/v1/logs/login-list',
    method: 'post',
    data: query
  })
}

export function exportLogin (query) {
  return request({
    url: '/admin/v1/logs/export-login',
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}

// 查询操作日志列表
export function findOperateList (query) {
  return request({
    url: '/admin/v1/logs/operate-list',
    method: 'post',
    data: query
  })
}

export function exportOperate (query) {
  return request({
    url: '/admin/v1/logs/export-operate',
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}
