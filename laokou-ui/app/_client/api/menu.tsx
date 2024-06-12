import request from "@/app/_client/utils/request";
//修改菜单
const updateMenu = async () => {
  const result = await request.put('/laokou/admin/v1/menus', { headers: { noLoading: true } });
  return result.data
}

//新增菜单
const addMenu = async () => {
  const result = await request.post('/laokou/admin/v1/menus', { headers: { noLoading: true } });
  return result.data
}
//查询菜单列表
const menuList = async () => {
  const result = await request.post('/laokou/admin/v1/menus/list', { headers: { noLoading: true } });
  return result.data
}
// 菜单树IDS

const getMenuIdsByRoleId = async (roleId:number) => {
  const result = await request.get('/laokou/admin/v1/menus/'+roleId+'/ids', { headers: { noLoading: true } });
  return result.data
}
// 查看菜单
const getMenuById = async (id:number) => {
  const result = await request.get('/laokou/admin/v1/menus/'+id, { headers: { noLoading: true } });
  return result.data
}

//删除菜单
const deleteMenuById = async (id:number) => {
  const result = await request.delete('/laokou/admin/v1/menus/'+id, { headers: { noLoading: true } });
  return result.data
}
//树形菜单列表
const getMenusTree = async () => {
  const result = await request.get('/laokou/admin/v1/menus/tree', { headers: { noLoading: true } });
  return result.data
}
//树形菜单列表 （用户）
const getUserMenusTree = async () => {
  const result = await request.get('/laokou/admin/v1/menus/tree-list', { headers: { noLoading: true } });
  return result.data
}
//树形租户菜单列表
const getTenantMenusTree = async () => {
  const result = await request.get('/laokou/admin/v1/menus/tenant-tree', { headers: { noLoading: true } });
  return result.data
}



