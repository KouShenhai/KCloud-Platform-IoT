import { request } from '@umijs/max'; 

// 查询岗位信息列表
export async function getPostList(params?: API.System.PostListParams) {
  return request<API.System.PostPageResult>('/api/system/post/list', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    params
  });
}

// 查询岗位信息详细
export function getPost(postId: number) {
  return request<API.System.PostInfoResult>(`/api/system/post/${postId}`, {
    method: 'GET'
  });
}

// 新增岗位信息
export async function addPost(params: API.System.Post) {
  return request<API.Result>('/api/system/post', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: params
  });
}

// 修改岗位信息
export async function updatePost(params: API.System.Post) {
  return request<API.Result>('/api/system/post', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: params
  });
}

// 删除岗位信息
export async function removePost(ids: string) {
  return request<API.Result>(`/api/system/post/${ids}`, {
    method: 'DELETE'
  });
}

// 导出岗位信息
export function exportPost(params?: API.System.PostListParams) { 
  return request<API.Result>(`/api/system/post/export`, {
    method: 'GET',
    params
  });
}
