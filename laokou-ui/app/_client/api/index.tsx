import request  from "@/app/_client/utils/request";

//查询分布式链路索引列表
const getIntexsTraceList = ():Promise<any> => {
    return request.get('/laokou/admin/v1/indexs/trace/list',{},{ headers: {noLoading: true} });  

}
//查询索引列表
const getIntexsList = ():Promise<any> => {
    return request.get('/laokou/admin//v1/indexs/list',{},{ headers: {noLoading: true} });  

}

//查看索引
const getIntexsTraceByIndexName = (indexName:string):Promise<any> => {
    return request.get('/laokou/admin//v1/indexs/'+indexName,{},{ headers: {noLoading: true} });  

}

//查看分布式链路索引
const getIntexsTraceById = (id:number):Promise<any> => {
    return request.get('/laokou/admin/v1/indexs/trace/'+id,{},{ headers: {noLoading: true} });  

}


