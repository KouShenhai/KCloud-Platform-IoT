import request  from "@/app/_client/utils/request";

interface RoleCO {
    /**
     * 部门IDS
     */
    deptIds?: number[];
    /**
     * ID
     */
    id?: number;
    /**
     * 菜单IDS
     */
    menuIds?: number[];
    /**
     * 角色名称
     */
    name?: string;
    /**
     * 角色排序
     */
    sort?: number;
}
//修改角色
const updateRole = async (params:RoleCO) => {
    const result = await request.put('/laokou/admin/v1/roles',params, { headers: { noLoading: true } });
    return result.data
}
//新增角色
const addRole= async (params:RoleCO) => {
    const result = await request.post('/laokou/admin/v1/roles',params, { headers: { noLoading: true } });
    return result.data
}
interface RoleType {
    /**
     * 结束时间
     */
    endTime?: string;
    /**
     * 忽略数据权限
     */
    ignore?: boolean;
    /**
     * 上一次ID，可以用于深度分页
     */
    lastId?: number;
    /**
     * 角色名称
     */
    name?: string;
    /**
     * 索引
     */
    pageIndex?: number;
    /**
     * 页码
     */
    pageNum?: number;
    /**
     * 条数
     */
    pageSize?: number;
    /**
     * SQL拼接
     */
    sqlFilter?: string;
    /**
     * 开始时间
     */
    startTime?: string;
}
//角色列表
const rolesList= async (params:RoleType) => {
    const result = await request.post('/laokou/admin/v1/roles/list',params, { headers: { noLoading: true } });
    return result.data
}

//角色查询
const searchRoleById = async (id:string) => {
    const result = await request.get('/laokou/admin/v1/roles/'+id,{}, { headers: { noLoading: true } });
    return result.data
}
//删除角色
const deleteRoleById = async (id:string) => {
    const result = await request.delete('/laokou/admin/v1/roles/'+id,{}, { headers: { noLoading: true } });
    return result.data
}
//角色下拉列表
const roleOptionList = async () => {
    const result = await request.get('/laokou/admin/v1/roles/option-list',{}, { headers: { noLoading: true } });
    return result.data
}