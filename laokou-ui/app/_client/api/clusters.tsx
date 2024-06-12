import request from "@/app/_client/utils/request";

// 查询服务列表
const serviceList = async () => {
    const result = await request.get('/laokou/admin/v1/clusters/service-list', { headers: { noLoading: true } });
    return result.data
  }

  // 查询实例列表
const instanceList = async () => {
    const result = await request.get('/laokou/admin/v1/clusters/instance-list', { headers: { noLoading: true } });
    return result.data
  }