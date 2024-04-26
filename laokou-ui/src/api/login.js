import request from '@/utils/request'

export const userApi = {
  Logout: '/admin/v1/logouts',
  Info: '/admin/v1/users/profile',
  Tenant: '/admin/v1/tenants/option-list'
}

export function login (params) {
  return request({
    url: '/auth/oauth2/token',
    method: 'post',
    data: params,
    // 设置序列化请求函数
    transformRequest: (data = {}) => Object.entries(data).map(ent => ent.join('=')).join('&'),
    headers: {
      'Authorization': 'Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=',
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
    }
  })
}

export function tenant () {
  return request({
    url: userApi.Tenant,
    method: 'get'
  })
}

export function getCaptcha (uuid) {
  return request({
    url: '/auth/v1/captchas/' + uuid,
    method: 'get'
  })
}

export function info () {
  return request({
    url: userApi.Info,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function logout (token) {
  return request({
    url: userApi.Logout,
    data: { token: token },
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getSecret () {
  return request({
    url: '/auth/v1/secrets',
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
