import request  from "@/app/_client/utils/request";

interface Login{
    username:string|false;
    password:string|false;
    captcha:string|undefined;
    uuid:string;
    tenant_id:number|undefined;
    grant_type: string;
    auth_type: 0
}

//登录
const login = async (params: Login)=> {
  const result = await request.post<any>('/laokou/auth/oauth2/token', params, { headers: {noLoading: true} });
  return result ;
}

export default login