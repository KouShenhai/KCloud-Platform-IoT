import request from '@/utils/request'

export function list (query) {
  return request({
    url: '/admin/v1/indexs/list',
    method: 'post',
    data: query
  })
}

export function findByIndexName (indexName) {
  return request({
    url: '/admin/v1/indexs/' + indexName,
    method: 'get'
  })
}

export function findTraceList (query) {
  return request({
    url: '/admin/v1/indexs/trace/list',
    method: 'post',
    data: query
  })
}

export function findTraceById (id) {
  return request({
    url: '/admin/v1/indexs/trace/' + id,
    method: 'get'
  })
}
