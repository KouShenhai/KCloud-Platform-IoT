import request  from "@/app/_client/utils/request";



//上报
const auditLogList = async () => {
    const result = await request.get('/laokou/admin/v1/reports',{}, { headers: { noLoading: true } });
    return result.data
}
