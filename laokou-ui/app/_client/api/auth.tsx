import request from "@/app/_client/utils/request";

// 生成令牌
const token = async () => {
    const result = await request.get('/laokou/admin/v1/tokens', { headers: { noLoading: true } });
    return result.data
  }