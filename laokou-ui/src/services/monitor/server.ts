import { request } from '@umijs/max'; 

/* *
 *
 * @author whiteshader@163.com
 * @datetime  2023/02/07
 * 
 * */


// 获取服务器信息
export async function getServerInfo() {
  return request('/api/monitor/server', {
    method: 'GET',
  });
}
