import request from '@/utils/request'

export function findServiceList (query) {
  return request({
    url: '/admin/v1/clusters/service-list',
    method: 'post',
    data: query
  })
}

export function findInstanceList (query) {
  return request({
    url: '/admin/v1/clusters/instance-list',
    method: 'post',
    data: query
  })
}

export function gracefulShutdown (r) {
  return request({
    url: '/' + r.router + '/graceful-shutdown',
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Service-Host': r.host,
      'Service-Port': r.port
    }
  })
}
