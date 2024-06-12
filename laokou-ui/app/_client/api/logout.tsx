import request from "@/app/_client/utils/request";
//退出登录
const logout = async () => {
  const result = await request.post('/laokou/admin/v1/logouts', { headers: { noLoading: true } });
  return result.data
}