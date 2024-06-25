import { request } from '@umijs/max';

// 查询定时任务调度列表
export async function getJobList(params?: API.Monitor.JobListParams) {
  return request<API.Monitor.JobPageResult>('/api/schedule/job/list', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params
  });
}

// 查询定时任务调度详细
export function getJob(jobId: number) {
  return request<API.Monitor.JobInfoResult>(`/api/schedule/job/${jobId}`, {
    method: 'GET'
  });
}

// 新增定时任务调度
export async function addJob(params: API.Monitor.Job) {
  return request<API.Result>('/api/schedule/job', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: params
  });
}

// 修改定时任务调度
export async function updateJob(params: API.Monitor.Job) {
  return request<API.Result>('/api/schedule/job', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: params
  });
}

// 删除定时任务调度
export async function removeJob(ids: string) {
  return request<API.Result>(`/api/schedule/job/${ids}`, {
    method: 'DELETE'
  });
}

// 导出定时任务调度
export function exportJob(params?: API.Monitor.JobListParams) {
  return request<API.Result>(`/api/schedule/job/export`, {
    method: 'GET',
    params
  });
}

// 定时任务立即执行一次
export async function runJob(jobId: number, jobGroup: string) {
  const job = {
    jobId,
    jobGroup,
  };
  return request('/api/schedule/job/run', {
    method: 'PUT',
    data: job,
  });
}
