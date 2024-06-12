  
import request from "@/app/_server/utils/request";
  
  //获取下拉列表 tenants/option-list
  const tenantsOptionList = async() => {
    const tenantsOptionList = await request.get('/laokou/admin/v1/tenants/option-list');
    return tenantsOptionList.data;
  }

  export default tenantsOptionList;