import { request } from '@umijs/max'; 

/**
 * 定时任务调度日志 API
 * 
 * @author whiteshader
 * @date 2023-02-07
 */

// 查询定时任务调度日志列表
export async function getJobLogList(params?: API.Monitor.JobLogListParams) {
  return request<API.Monitor.JobLogPageResult>('/api/schedule/job/log/list', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params
  });
}
 

// 删除定时任务调度日志
export async function removeJobLog(jobLogId: string) {
  return request<API.Result>(`/api/schedule/job/log/${jobLogId}`, {
    method: 'DELETE'
  });
}

// 清空调度日志
export function cleanJobLog() {
  return request('/api/schedule/job/log/clean', {
    method: 'delete'
  })
}

// 导出定时任务调度日志
export function exportJobLog(params?: API.Monitor.JobLogListParams) { 
  return request<API.Result>(`/api/schedule/job/log/export`, {
    method: 'GET',
    params
  });
}