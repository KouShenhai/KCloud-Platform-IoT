import request from "@/app/_client/utils/request";


//新增ip白名单
const addIPwhite = async () => {
    const result = await request.post('/laokou/admin/v1/ips/white', { headers: { noLoading: true } });
    return result.data
  }
//查询ip白名单
const ipWhiteList = async () => {
    const result = await request.post('/laokou/admin/v1/ips/white/list', { headers: { noLoading: true } });
    return result.data
  }
//新增ip黑名单
const addIpBlack = async () => {
    const result = await request.post('/laokou/admin/v1/ips/black', { headers: { noLoading: true } });
    return result.data
  }

//ip黑名单列表
const ipBlackList = async () => {
    const result = await request.post('/laokou/admin/v1/ips/black/list', { headers: { noLoading: true } });
    return result.data
  }
//刷新ip白名单
const refreshIpWhite = async (label:string) => {
    const result = await request.post('/laokou/admin/v1/ips/white/refresh/'+label, { headers: { noLoading: true } });
    return result.data
  }
//刷新ip 黑名单
const refreshIpBlack = async (label:string) => {
    const result = await request.post('/laokou/admin/v1/ips/white/'+label, { headers: { noLoading: true } });
    return result.data
  }
//删除ip白名单
const deleteIpWhiteById = async (id:number) => {
    const result = await request.delete('/laokou/admin/v1/ips/white/'+id, { headers: { noLoading: true } });
    return result.data
  }
//删除IP黑名单
const deleteIpBlackById = async (id:number) => {
    const result = await request.delete('/laokou/admin/v1/ips/black/'+id, { headers: { noLoading: true } });
    return result.data
  }