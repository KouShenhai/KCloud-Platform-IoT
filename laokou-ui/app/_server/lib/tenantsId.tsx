import request from "@/app/_server/utils/request";

//获取租户id
const tenantsId = async () => {

    const tenantsId = await request.get( '/laokou/admin/v1/tenants/id');
  
    return tenantsId.data;
  }

export default tenantsId;