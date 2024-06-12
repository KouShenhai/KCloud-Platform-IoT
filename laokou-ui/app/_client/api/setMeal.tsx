import request  from "@/app/_client/utils/request";
interface PackageCO {
    /**
     * ID
     */
    id?: number;
    /**
     * 菜单IDS
     */
    menuIds?: number[];
    /**
     * 套餐名称
     */
    name?: string;
}
//修改套餐
const updateSetMeal= async (params:PackageCO) => {
    const result = await request.put('/laokou/admin/v1/packages',params, { headers: { noLoading: true } });
    return result.data
}
//新增套餐
const addSetMeal= async (params:PackageCO) => {
    const result = await request.post('/laokou/admin/v1/packages',params, { headers: { noLoading: true } });
    return result.data
}
interface SetMeal {
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
     * 套餐名称
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
//套餐列表
const setMealList= async (params:SetMeal) => {
    const result = await request.post('/laokou/admin/v1/packages/list',params, { headers: { noLoading: true } });
    return result.data
}
//查看套餐
const searchSetMealById= async (id:string) => {
    const result = await request.get('/laokou/admin/v1/packages/'+id,{}, { headers: { noLoading: true } });
    return result.data
}

//删除套餐
const deleteSetMealById= async (id:string) => {
    const result = await request.delete('/laokou/admin/v1/packages/'+id,{}, { headers: { noLoading: true } });
    return result.data
}

//下拉列表
const setMealOptionList= async () => {
    const result = await request.get('/laokou/admin/v1/packages/option-list',{}, { headers: { noLoading: true } });
    return result.data
}