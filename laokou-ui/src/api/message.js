import request from '@/utils/request'

export function list (query) {
  return request({
    url: '/admin/v1/messages/list',
    method: 'post',
    data: query
  })
}

export function findUnreadList (query) {
  return request({
    url: '/admin/v1/messages/unread-list',
    method: 'post',
    data: query
  })
}

export function create (data, token) {
  return request({
    url: '/admin/v1/messages',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'Request-Id': token
    }
  })
}

export function findUnreadCount () {
  return request({
    url: '/admin/v1/messages/unread-count',
    method: 'get'
  })
}

export function findByDetailId (id) {
  return request({
    url: '/admin/v1/messages/' + id,
    method: 'post'
  })
}

export function findById (id) {
  return request({
    url: '/admin/v1/messages/' + id,
    method: 'get'
  })
}
