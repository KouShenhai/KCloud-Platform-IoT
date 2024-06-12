import request  from "@/app/_client/utils/request";

//登录
const image = (uuid:string):Promise<any> => {
    return request.get('/laokou/auth/v1/captchas/'+uuid,{},{ headers: {noLoading: true} });  

}
  export default image