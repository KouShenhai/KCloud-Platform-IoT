import { request } from '@umijs/max';

// 查询在线用户列表
export async function getOnlineUserList(params?: API.Monitor.OnlineUserListParams) {
  return request<API.Monitor.OnlineUserPageResult>('/api/system/online/list', {
    method: 'GET',
    params,
  });
}

// 强退用户
export async function forceLogout(tokenId: string) {
  return request(`/api/system/online/${tokenId}`, {
    method: 'DELETE',
  });
}
