import request  from "@/app/_client/utils/request";

interface ResourceCO {
    /**
     * 资源类型 audio音频 video视频  image图片
     */
    code: string;
    /**
     * ID
     */
    id?: number;
    /**
     * 实例ID
     */
    instanceId?: string;
    /**
     * 资源备注
     */
    remark: string;
    /**
     * 资源审批状态 0待审批 1审批中 -1驳回审批 2通过审批
     */
    status: number;
    /**
     * 资源名称
     */
    title: string;
    /**
     * 资源的URL
     */
    url: string;
}

//修改资源
const updataResource= async (params:ResourceCO) => {
    const result = await request.put('/laokou/admin/v1/resource',params, { headers: { noLoading: true } });
    return result.data
}
//新增资源
const addResource= async (params:ResourceCO) => {
    const result = await request.post('/laokou/admin/v1/resource',params, { headers: { noLoading: true } });
    return result.data
}

//上传资源
const uploadResource= async (files:FileList) => {
    const result = await request.post('/laokou/admin/v1/resource/upload',files, { headers: { noLoading: true,'Content-Type':'multipart/form-data' } });
    return result.data
}
interface TaskCO {
    /**
     * 业务Key
     */
    businessKey?: number;
    /**
     * 实例名称
     */
    instanceName?: string;
    /**
     * 任务ID
     */
    taskId?: string;
    /**
     * 用户ID
     */
    userId?: number;
}

//转办任务
const transferTask= async (params:TaskCO) => {
    const result = await request.post('/laokou/admin/v1/resource/transfer-task',params, { headers: { noLoading: true } });
    return result.data
}

// 查询任务列表
const searchTaskList = async (params:TaskCO) => {
    const result = await request.post('/laokou/admin/v1/resource/task-list',params, { headers: { noLoading: true } });
    return result.data
}
//同步资源
const syncResource = async (params:TaskCO) => {
    const result = await request.post('/laokou/admin/v1/resource/sync',params, { headers: { noLoading: true } });
    return result.data
}
interface Search {
    aggregationKey?: Aggregation;
    /**
     * 索引名称
     */
    indexNames: string[];
    /**
     * or查询集合
     */
    orQueryList?: Query[];
    /**
     * 页码
     */
    pageNum?: number;
    /**
     * 条数
     */
    pageSize?: number;
    /**
     * 分词搜索集合
     */
    queryStringList?: Query[];
    /**
     * 排序属性集合
     */
    sortFieldList?: Query[];
}

/**
 * Aggregation，聚合
 */
export interface Aggregation {
    /**
     * 属性
     */
    field?: string;
    /**
     * 分组Key
     */
    groupKey?: string;
    /**
     * 脚本
     */
    script?: string;
}

/**
 * Query，查询
 */
export interface Query {
    /**
     * 属性
     */
    field?: string;
    /**
     * 值
     */
    value?: string;
}

//搜索资源
const searchResource = async (params:Search) => {
    const result = await request.post('/laokou/admin/v1/resource/search',params, { headers: { noLoading: true } });
    return result.data
}

interface Request {
    /**
     * 业务Key
     */
    businessKey?: number;
    /**
     * 实例ID
     */
    instanceId?: string;
    /**
     * 实例名称
     */
    instanceName?: string;
    /**
     * 任务ID
     */
    taskId?: string;
    [property: string]: any;
}
// 处理任务
const resolveTask = async (params:Request) => {
    const result = await request.post('/laokou/admin/v1/resource/resolve-task',params, { headers: { noLoading: true } });
    return result.data
}
interface SearchResouce {
    /**
     * 资源类型 audio音频 video视频  image图片
     */
    code?: string;
    /**
     * 结束时间
     */
    endTime?: string;
    /**
     * ID
     */
    id?: number;
    /**
     * 忽略数据权限
     */
    ignore?: boolean;
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
     * 资源审批状态 0待审批 1审批中 -1驳回审批 2通过审批
     */
    status?: number;
    /**
     * 资源名称
     */
    title?: string;
}
// 查询资源列表
const searchResouceList = async (params:SearchResouce) => {
    const result = await request.post('/laokou/admin/v1/resource/list',params, { headers: { noLoading: true } });
    return result.data
}
interface Delegate {
    /**
     * 业务Key
     */
    businessKey?: number;
    /**
     * 实例名称
     */
    instanceName?: string;
    /**
     * 任务ID
     */
    taskId?: string;
    /**
     * 用户ID
     */
    userId?: number;
}

// 委派任务
const delegateTask = async (params:Delegate) => {
    const result = await request.post('/laokou/admin/v1/resource/delegate-task',params, { headers: { noLoading: true } });
    return result.data
}
interface Audit {
    /**
     * 业务Key
     */
    businessKey?: number;
    /**
     * 审批意见
     */
    comment?: string;
    /**
     * 实例ID
     */
    instanceId?: string;
    /**
     * 实例名称
     */
    instanceName?: string;
    /**
     * 任务ID
     */
    taskId?: string;
    /**
     * 任务名称
     */
    taskName?: string;
    /**
     * 流程变量
     */
    values?: { [key: string]: { [key: string]: any } };
}
// 审批任务
const auditTask = async (params:Audit) => {
    const result = await request.post('/laokou/admin/v1/resource/audit-task',params, { headers: { noLoading: true } });
    return result.data
}


// 查看流程
const diagramSee = async (instanceId:string) => {
    const result = await request.get('/laokou/admin/v1/resource/'+instanceId+'/diagram',{}, { headers: { noLoading: true } });
    return result.data
}
// 查看资源
const searchResouceById = async (id:string) => {
    const result = await request.get('/laokou/admin/v1/resource/'+id,{}, { headers: { noLoading: true } });
    return result.data
}
//删除资源
const deleteResouceById = async (id:string) => {
    const result = await request.delete('/laokou/admin/v1/resource/'+id,{}, { headers: { noLoading: true } });
    return result.data
}

//下载资源
const downloadResouceById = async (id:string) => {
    const result = await request.get('/laokou/admin/v1/resource/'+id+'/download',{}, { headers: { noLoading: true } });
    return result.data
}
//查看任务
const searchTask = async (id:string) => {
    const result = await request.get('/laokou/admin/v1/resource/'+id+'/detail-task',{}, { headers: { noLoading: true } });
    return result.data
}
//查询审批日志列表
const auditLogList = async (id:string) => {
    const result = await request.get('/laokou/admin/v1/resource/'+id+'/audit-log',{}, { headers: { noLoading: true } });
    return result.data
}




