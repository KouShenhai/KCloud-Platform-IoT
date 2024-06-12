import request  from "@/app/_client/utils/request";

interface LogCO {
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
     * 操作的模块名称
     */
    moduleName?: string;
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
     * 操作状态 0成功 1失败
     */
    status?: number;
}

//查询操作日志列表
const logOperateList = async (params:LogCO) => {
    const result = await request.post('/laokou/admin/v1/logs/operate-list', params,{ headers: { noLoading: true } });
    return result.data
  }
//导出操作日志
const logOperateExport = async (params:LogCO) => {
    const result = await request.post('/laokou/admin/v1/logs/operate-export', params, { headers: { noLoading: true } });
    return result.data
  }
  interface LoginCO {
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
     * 登录状态 0登录成功 1登录失败
     */
    status?: number;
    /**
     * 登录的用户名
     */
    username?: string;
}

//查询登录日志列表
const logLoginList = async (params:LoginCO) => {
    const result = await request.post('/laokou/admin/v1/logs/login-list',  params,{ headers: { noLoading: true } });
    return result.data
  }
//导出登录日志
const logsLoginExport = async (params:LoginCO) => {
    const result = await request.post('/laokou/admin/v1/logs/login-export',  params,{ headers: { noLoading: true } });
    return result.data
  }