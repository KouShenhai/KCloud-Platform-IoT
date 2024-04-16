import request from '@/utils/request'

export const menuApi = {
  Router: '/admin/v1/menus/list'
}

// 获取路由
export const getRouters = () => {
  return request({
    url: menuApi.Router,
    method: 'post',
    data: { name: '', type: 'USER_TREE_LIST' },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
