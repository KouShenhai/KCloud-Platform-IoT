import { request } from '@umijs/max'; 

// 查询通知公告列表
export async function getNoticeList(params?: API.System.NoticeListParams) {
  return request<API.System.NoticePageResult>('/api/system/notice/list', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params
  });
}

// 查询通知公告详细
export function getNotice(noticeId: number) {
  return request<API.System.NoticeInfoResult>(`/api/system/notice/${noticeId}`, {
    method: 'GET'
  });
}

// 新增通知公告
export async function addNotice(params: API.System.Notice) {
  return request<API.Result>('/api/system/notice', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: params
  });
}

// 修改通知公告
export async function updateNotice(params: API.System.Notice) {
  return request<API.Result>('/api/system/notice', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: params
  });
}

// 删除通知公告
export async function removeNotice(ids: string) {
  return request<API.Result>(`/api/system/notice/${ids}`, {
    method: 'DELETE'
  });
}

// 导出通知公告
export function exportNotice(params?: API.System.NoticeListParams) { 
  return request<API.Result>(`/api/system/notice/export`, {
    method: 'GET',
    params
  });
}
