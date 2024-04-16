import request from '@/utils/request'

export const userApi = {
  Token: '/auth/oauth2/token',
  Logout: '/admin/v1/logouts',
  Out: '/auth/logout?logout',
  Info: '/admin/v1/users/profile',
  Captcha: '/auth/v1/captchas/',
  Tenant: '/admin/v1/tenants/option-list',
  Secret: '/auth/v1/secrets'
}

export function login (params) {
  return request({
    url: userApi.Token,
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

export function out () {
  return request({
    url: userApi.Out,
    method: 'get'
  })
}

export function tenant () {
  return request({
    url: userApi.Tenant,
    method: 'get'
  })
}

export function captcha (uuid) {
  return request({
    url: userApi.Captcha + uuid,
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

export function secret () {
  return request({
    url: userApi.Secret,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
