import request from "@/app/_client/utils/request";

interface SourceCO {
    /**
     * 数据源的驱动名称
     */
    driverClassName?: string;
    /**
     * ID
     */
    id?: number;
    /**
     * 数据源名称
     */
    name?: string;
    /**
     * 数据源的密码
     */
    password?: string;
    /**
     * 数据源的连接信息
     */
    url?: string;
    /**
     * 数据源的用户名
     */
    username?: string;
    [property: string]: any;
}

//修改数据源
const updataDbSource = async (params:SourceCO) => {
    const result = await request.put('/laokou/admin/v1/sources',params, { headers: { noLoading: true } });
    return result.data
  }

//新增数据源
const addDefinitions = async (params:SourceCO) => {
    const result = await request.post('/laokou/admin/v1/sources',params, { headers: { noLoading: true } });
    return result.data
  }
  interface SearchSourceList {
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
     * 数据源名称
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
    [property: string]: any;
}

//查询数据源列表
const searchSourceList = async (params:SearchSourceList) => {
    const result = await request.post('/laokou/admin/v1/sources/list',params, { headers: { noLoading: true } });
    return result.data
  }

//查看数据源
const searchSourceById = async (id:string) => {
    const result = await request.get('/laokou/admin/v1/sources/'+id,{}, { headers: { noLoading: true } });
    return result.data
  }
//删除数据源
const deletesSourceById = async (id:string) => {
    const result = await request.delete('/laokou/admin/v1/sources/'+id,{}, { headers: { noLoading: true } });
    return result.data
  }

//下拉列表
const sourcesOptionList = async () => {
    const result = await request.get('/laokou/admin/v1/sources/option-list',{}, { headers: { noLoading: true } });
    return result.data
  }