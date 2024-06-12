import request  from "@/app/_client/utils/request";


let promise:Promise<any>;
//登录
const refreshToken = ():Promise<any> => {
    if(promise){
        return promise;
    }
    promise = new Promise<any>(async (resolve) => {
       const resp = await request.post<any>('/laokou/auth/oauth2/token', {grant_type:"refresh_token", refresh_token: ""}, { headers: {noLoading: true,__isRefreshToken:true} }) ;  
         resolve( resp.code);
    })
    
    promise.finally(()=> null)
  
    return promise;
}



  export {refreshToken}