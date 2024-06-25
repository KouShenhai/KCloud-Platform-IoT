import { request } from '@umijs/max';


// 获取服务器信息
export async function getServerInfo() {
  return request('/api/monitor/server', {
    method: 'GET',
  });
}
