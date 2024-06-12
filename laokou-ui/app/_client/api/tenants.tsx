import request  from "@/app/_client/utils/request";

interface TenantCO {
    /**
     * ID
     */
    id?: number;
    /**
     * 租户标签
     */
    label?: string;
    /**
     * 租户名称
     */
    name?: string;
    /**
     * 套餐ID
     */
    packageId?: number;
    /**
     * 数据源ID
     */
    sourceId?: number;
}

//修改租户
const updateTenant = async (params:TenantCO) => {
    const result = await request.put('/laokou/admin/v1/tenants', params, { headers: { noLoading: true } });
    return result.data
  }
//新增租户
const addTenant = async (params:TenantCO) => {
    const result = await request.post('/laokou/admin/v1/tenants', params, { headers: { noLoading: true } });
    return result.data
  }
//查询租户列表
const searcheTenantList = async (params:TenantCO) => {
    const result = await request.post('/laokou/admin/v1/tenants/list', params, { headers: { noLoading: true } });
    return result.data
  }
//查看租户
const searcheTenantById = async (id:string) => {
    const result = await request.get('/laokou/admin/v1/tenants/'+id,{}, { headers: { noLoading: true } });
    return result.data
  }
//删除租户
const deleteTenantById = async (id:string) => {
    const result = await request.get('/laokou/admin/v1/tenants/'+id,{}, { headers: { noLoading: true } });
    return result.data
  }
//下载数据库
const downloadDatasourceById = async (id:string) => {
    const result = await request.get('/laokou/admin/v1/tenants/'+id+'/download-datasource',{}, { headers: { noLoading: true } });
    return result.data
  }
//下拉列表
const optionList = async () => {
  const result = await request.get('/laokou/admin/v1/tenants/option-list',{}, { headers: { noLoading: true } });
  return result.data
}

//解析域名查看ID
const viewDomainName= async (id:string) => {
  const result = await request.get('/laokou/admin/v1/tenants/'+id,{}, { headers: { noLoading: true } });
  return result.data
}