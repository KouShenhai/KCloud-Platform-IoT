import request  from "@/app/_client/utils/request";
interface DictCO {
    /**
     * 创建时间
     */
    createDate?: Date;
    /**
     * ID
     */
    id?: number;
    /**
     * 字典标签
     */
    label?: string;
    /**
     * 字典备注
     */
    remark?: string;
    /**
     * 字典排序
     */
    sort?: number;
    /**
     * 字典类型
     */
    type?: string;
    /**
     * 字典值
     */
    value?: string;
    [property: string]: any;
}

//修改字典
const updataDicts = async (params:DictCO) => {
    const result = await request.put('/laokou/admin/v1/dicts',params, { headers: { noLoading: true } });
    return result.data
  }

//新增字典
const addDicts = async (params:DictCO) => {
    const result = await request.post('/laokou/admin/v1/dicts',params, { headers: { noLoading: true } });
    return result.data
  }
  interface SearchDictsList {
    /**
     * 结束时间
     */
    endTime?: string;
    /**
     * 忽略数据权限
     */
    ignore?: boolean;
    /**
     * 字典标签
     */
    label?: string;
    /**
     * 上一次ID，可以用于深度分页
     */
    lastId?: number;
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
    /**
     * 字典类型
     */
    type?: string;
}

//查询字典列表
const searchDictsList = async (params:SearchDictsList) => {
    const result = await request.post('/laokou/admin/v1/dicts/list',params, { headers: { noLoading: true } });
    return result.data
  }

//下拉列表
const dictsOptionList = async (type:string) => {
    const result = await request.get('/laokou/admin/v1/dicts/'+type+'/option-list',{}, { headers: { noLoading: true } });
    return result.data
  }

//查看字典
const searchDictsById = async (id:string) => {
    const result = await request.get('/laokou/admin/v1/dicts/'+id,{}, { headers: { noLoading: true } });
    return result.data
  }
// 删除字典
const deleteDictById = async (id:string) => {
    const result = await request.delete('/laokou/admin/v1/dicts/'+id,{}, { headers: { noLoading: true } });
    return result.data
  }