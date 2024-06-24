import { request } from '@umijs/max'; 


/* *
 *
 * @author whiteshader@163.com
 * @datetime  2021/09/16
 * 
 * */


// 获取服务器信息
export async function getCacheInfo() {
  return request<API.Monitor.CacheInfoResult>('/api/monitor/cache', {
    method: 'GET',
  });
}
