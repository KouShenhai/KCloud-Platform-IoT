import request from "@/app/_server/utils/request";
//获取公钥
const imgage = async (uuid:string) => {
    const rest = await request.get('/laokou/auth/v1/captchas/'+uuid);  
    return rest.data;
  }
export  default imgage