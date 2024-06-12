import request from "@/app/_server/utils/request";
//获取公钥
const getPublicKey = async () => {
    const rest = await request.get('/laokou/auth/v1/secrets');  
    return rest.data;
  }
export  default getPublicKey