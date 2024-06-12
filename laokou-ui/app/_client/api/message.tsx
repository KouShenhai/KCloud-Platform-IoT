import request from "@/app/_client/utils/request";

// 新增消息
const addMessage = async () => {
    const result = await request.get('/laokou/admin/v1/messages', { headers: { noLoading: true } });
    return result.data
  }
//消息详情
  const messageDetail = async (detailId:string) => {
    const result = await request.get('/laokou/admin/v1/messages/'+detailId, { headers: { noLoading: true } });
    return result.data
  }
  //未读消息列表
  const unreadList = async () => {
    const result = await request.get('/laokou/admin/v1/messages/unread-list', { headers: { noLoading: true } });
    return result.data
  }
  //查询消息列表
  const messageList = async () => {
    const result = await request.get('/laokou/admin/v1/messages/list', { headers: { noLoading: true } });
    return result.data
  }
  //查看消息
  const getMessage = async (id:number) => {
    const result = await request.get('/laokou/admin/v1/messages/'+id, { headers: { noLoading: true } });
    return result.data
  }
  //未读消息统计
  const unReadCount = async () => {
    const result = await request.get('/laokou/admin/v1/messages/unread-count', { headers: { noLoading: true } });
    return result.data
  }
  