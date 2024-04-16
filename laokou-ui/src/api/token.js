import request from '@/utils/request'

// 获取token
export function getToken () {
  return request({
    url: '/admin/v1/tokens',
    method: 'post'
  })
}
