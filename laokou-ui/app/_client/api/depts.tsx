import request  from "@/app/_client/utils/request";
interface DeptCO {
    /**
     * 子节点
     */
    children?: DeptCO[];
    /**
     * ID
     */
    id?: number;
    /**
     * 名称
     */
    name?: string;
    /**
     * 节点PATH
     */
    path?: string;
    /**
     * 父节点ID
     */
    pid?: number;
    /**
     * 部门排序
     */
    sort?: number;
}
//修改菜单
const updateDepts = async (params:DeptCO) => {
    const result = await request.put('/laokou/admin/v1/depts',params, { headers: { noLoading: true } });
    return result.data
  }

//新增菜单
const addDpets = async (params:DeptCO) => {
    const result = await request.post('/laokou/admin//v1/depts',params, { headers: { noLoading: true } });
    return result.data
  }
//查询菜单列表
const searchDeptsByName = async (name:string) => {
    const result = await request.post('/laokou/admin/v1/depts/list',{name:name}, { headers: { noLoading: true } });
    return result.data
  }
//部门IDS
const searchDeptsIdsByRoleId = async (roleId:string) => {
    const result = await request.get('/laokou/admin/v1/depts/'+roleId+'/ids',{}, { headers: { noLoading: true } });
    return result.data
  }
//查看菜单
const searchDpetsById = async (id:string) => {
    const result = await request.get('/laokou/admin/v1/depts/'+id,{}, { headers: { noLoading: true } });
    return result.data
  }
//删除菜单
const deleteDeptsById = async (id:string) => {
    const result = await request.delete('/laokou/admin/v1/depts/'+id,{}, { headers: { noLoading: true } });
    return result.data
  }
//树形部门列表
const deptsTree = async () => {
    const result = await request.get('/laokou/admin/v1/depts/tree',{}, { headers: { noLoading: true } });
    return result.data
  }