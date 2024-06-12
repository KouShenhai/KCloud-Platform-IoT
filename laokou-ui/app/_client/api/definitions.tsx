import request  from "@/app/_client/utils/request";


//挂起流程
const suspend = async (definitionId:string) => {
    const result = await request.put('/laokou/admin/v1/definitions/'+definitionId+'/suspend',{}, { headers: { noLoading: true } });
    return result.data
  }
//激活流程
const activate = async (definitionId:string) => {
    const result = await request.put('/laokou/admin/v1/definitions/'+definitionId+'/activate',{}, { headers: { noLoading: true } });
    return result.data
  }
//新增流程
const addDefinitions = async (file:FileList) => {
    const result = await request.post('/laokou/admin/v1/definitions',file, { headers: { noLoading: true } });
    return result.data
  }
  interface Definitions {
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
     * 流程名称
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

// 查询流程列表
const definitionsList = async (params:Definitions) => {
    const result = await request.post('/laokou/admin/v1/definitions/list',{}, { headers: { noLoading: true } });
    return result.data
  }
//流程图
const diagram = async (definitionId:string) => {
    const result = await request.get('/laokou/admin/v1/definitions/'+definitionId+'/diagram',{}, { headers: { noLoading: true } });
    return result.data
  }
//流程模板
const template = async () => {
    const result = await request.get('/laokou/admin/v1/definitions/template',{}, { headers: { noLoading: true } });
    return result.data
  }
//删除流程
const deleteDefinitionsById = async (deploymentId:string) => {
    const result = await request.delete('/laokou/admin/v1/definitions/'+deploymentId,{}, { headers: { noLoading: true } });
    return result.data
  }